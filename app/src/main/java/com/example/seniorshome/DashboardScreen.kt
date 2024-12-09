@file:JvmName("TaskKt")

package com.example.seniorshome

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ChildEventListener

@Composable
fun DashboardScreen(navController: NavController) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    val context = LocalContext.current  // Get the context here within the composable

    // Firebase database reference
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tasks")

    // Fetch tasks from Firebase
    LaunchedEffect(Unit) {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue(Task::class.java)
                if (task != null) {
                    tasks = tasks + task // Add new task to the list
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedTask = snapshot.getValue(Task::class.java)
                if (updatedTask != null) {
                    tasks = tasks.map { if (it.id == updatedTask.id) updatedTask else it }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedTask = snapshot.getValue(Task::class.java)
                if (removedTask != null) {
                    tasks = tasks.filter { it.id != removedTask.id }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Log.e("DashboardScreen", "Error loading tasks", error.toException())
            }
        })
    }

    // Sort tasks by time
    val sortedTasks = tasks.sortedBy { it.time }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Logo and greeting
        DashboardHeader()

        // Title "BULOHATON" styled similarly to Notifications
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Scrollable Task List
            TaskList(
                tasks = sortedTasks,
                onDeleteTask = { task ->
                    taskToDelete = task
                    showDialog = true
                },
                onToggleTask = { task, isOn ->
                    updateTaskStatusInFirebase(database, task, isOn)
                    tasks = tasks.map { if (it == task) it.copy(isOn = isOn) else it }

                    if (isOn) {
                        scheduleAlarm(
                            context = context,
                            taskName = task.name,
                            taskTime = task.time,
                            alarmSoundUri = task.alarmSound,
                            days = task.days,
                            phoneNumber = task.familyPhoneNumber
                        )
                    } else {
                        cancelAlarm(context, task.name, task.days)
                    }
                },
                navController = navController
            )
        }

        // Bottom Navigation
        BottomNavigationWithExpandableFAB(navController = navController, currentScreen = "dashboard")
    }

    // Delete confirmation dialog
    if (showDialog && taskToDelete != null) {
        DeleteTaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                taskToDelete?.let { task ->
                    deleteTaskFromFirebase(database, task)
                    tasks = tasks.filter { it != task }
                    cancelAlarm(context, task.name, task.days)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun DeleteTaskDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Delete this task?",
                color = Color.Black, // Set text color to Black
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    // Call the confirm action directly
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Set DELETE button color to Red
            ) {
                Text("Delete", color = Color.White) // DELETE text in white
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Black) // Set CANCEL text color to Black
            }
        },
        containerColor = Color.White, // Set dialog background to White
        modifier = Modifier.size(363.dp, 150.dp) // Set dialog size
    )
}


@Composable
fun DashboardHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.shlogo), // Replace with your logo
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Seniors Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00)
            )
        }
        Divider(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onDeleteTask: (Task) -> Unit,
    onToggleTask: (Task, Boolean) -> Unit,
    navController: NavController // Add this parameter
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onToggle = { isOn -> onToggleTask(task, isOn) },
                onDelete = { onDeleteTask(task) },
                navController = navController // Pass navController
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    navController: NavController // Add this parameter
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF004d00), shape = MaterialTheme.shapes.medium)
            .clickable {
                navController.navigate("addtask") // Navigate to AddTaskScreen
            }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = task.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = task.time,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = task.days.joinToString(", "), // Convert the List<String> to a comma-separated String
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (task.isOn) "ON" else "OFF",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = task.isOn,
                        onCheckedChange = { isChecked ->
                            // Call onToggle to update task status
                            onToggle(isChecked)

                            // Play the appropriate sound when switching on or off
                            val soundRes = if (isChecked) R.raw.alarmon else R.raw.alarmoff
                            val mediaPlayer = MediaPlayer.create(context, soundRes)
                            mediaPlayer.start()

                            // Release MediaPlayer resources after playback
                            mediaPlayer.setOnCompletionListener {
                                mediaPlayer.release()
                            }

                            // Trigger alarm logic based on the switch state
                            if (isChecked) {
                                // Schedule alarm when turned on
                                scheduleAlarm(
                                    context = context,
                                    taskName = task.name,
                                    taskTime = task.time,
                                    alarmSoundUri = task.alarmSound,
                                    days = task.days,
                                    phoneNumber = task.familyPhoneNumber
                                )
                            } else {
                                // Cancel alarm when turned off
                                cancelAlarm(context, task.name, task.days)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Black,
                            checkedTrackColor = Color.Black,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                IconButton(
                    onClick = {
                        val mediaPlayer = MediaPlayer.create(context, R.raw.deletetask) // Sound file from res/raw folder
                        mediaPlayer.start()

                        // Release MediaPlayer resources after playback
                        mediaPlayer.setOnCompletionListener {
                            mediaPlayer.release()
                        }

                        // Execute the onDelete action after playing the sound
                        onDelete()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}


// Firebase Functions
fun deleteTaskFromFirebase(database: DatabaseReference, task: Task) {
    val taskId = task.id
    if (taskId != null) {
        database.child(taskId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("DashboardScreen", "Task deleted successfully")
            } else {
                Log.e("DashboardScreen", "Error deleting task", task.exception)
            }
        }
    }
}

fun updateTaskStatusInFirebase(database: DatabaseReference, task: Task, isOn: Boolean) {
    val taskId = task.id
    if (taskId != null) {
        database.child(taskId).child("isOn").setValue(isOn).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("DashboardScreen", "Task status updated")
            } else {
                Log.e("DashboardScreen", "Error updating task status", task.exception)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
