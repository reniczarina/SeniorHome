@file:JvmName("MedicationKt")

package com.example.seniorshome

import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

// Data class to represent Medication
data class Medication(
    val id: String = "",
    val name: String = "",
    val time: String = "",
    val days: List<String> = emptyList(), // Change to List<String>
    val alarmEnabled: Boolean = false,
    val alarmSound: String = "",
    val notifyFamily: Boolean = false,
    val isOn: Boolean = false
)

@Composable
fun MedicationScreen(navController: NavController) {
    var medications by remember { mutableStateOf<List<Medication>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var medicationToDelete by remember { mutableStateOf<Medication?>(null) }
    val context = LocalContext.current  // Get the context here within the composable

    // Firebase database reference
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("medication")

    // Fetch medications from Firebase
    LaunchedEffect(Unit) {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val medication = snapshot.getValue(Medication::class.java)
                if (medication != null) {
                    medications = medications + medication
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMedication = snapshot.getValue(Medication::class.java)
                if (updatedMedication != null) {
                    medications = medications.map { if (it.id == updatedMedication.id) updatedMedication else it }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedMedication = snapshot.getValue(Medication::class.java)
                if (removedMedication != null) {
                    medications = medications.filter { it.id != removedMedication.id }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Log.e("MedicationScreen", "Error loading medications", error.toException())
            }
        })
    }

    // Sort medications by time
    val sortedMedications = medications.sortedBy { parseTime(it.time) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Logo and greeting
        Header()

        // Title "Medications"
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Medications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Scrollable Medication List
            MedicationList(
                medications = sortedMedications,
                onDeleteMedication = { medication ->
                    medicationToDelete = medication
                    showDialog = true
                },
                onToggleMedication = { medication, isOn ->
                    // Update the medication status in the Firebase database
                    updateMedicationStatusInFirebase(database, medication, isOn)

                    // Update the local list of medications
                    medications = medications.map {
                        if (it == medication) it.copy(isOn = isOn) else it
                    }

                    // Manage alarms based on the toggle status
                    if (isOn) {
                        scheduleAlarm(
                            context = context,
                            taskName = medication.name,
                            taskTime = medication.time,
                            alarmSoundUri = medication.alarmSound,
                            days = medication.days
                        )
                    } else {
                        cancelAlarm(context, medication.name, medication.days)
                    }
                },
                navController = navController

            )
        }

        // Bottom Navigation
        BottomNavigationWithExpandableFAB(navController = navController, currentScreen = "medicine")
    }

    // Delete confirmation dialog
    if (showDialog && medicationToDelete != null) {
        DeleteMedicationDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                medicationToDelete?.let { medication ->
                    deleteMedicationFromFirebase(database, medication)
                    medications = medications.filter { it != medication }
                    cancelAlarm(context, medication.name, medication.days)
                }
                showDialog = false
            }
        )
    }
}


@Composable
fun DeleteMedicationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
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
                onClick = onConfirm,
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
fun MedicationList(
    medications: List<Medication>,
    onDeleteMedication: (Medication) -> Unit,
    onToggleMedication: (Medication, Boolean) -> Unit,
    navController: NavController // Add NavController for navigation
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(medications) { medication ->
            MedicationItem(
                medication = medication,
                onToggle = { isOn -> onToggleMedication(medication, isOn) },
                onDelete = { onDeleteMedication(medication) },
                navController = navController // Pass navController for navigation
            )
        }
    }
}


@Composable
fun MedicationItem(
    medication: Medication,
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
                navController.navigate("addmedicationscreen") // Navigate to AddMedicationScreen
            }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = medication.name,
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
                        text = medication.time,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = medication.days.joinToString(", "), // Convert the List<String> to a comma-separated String
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (medication.isOn) "ON" else "OFF",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = medication.isOn,
                        onCheckedChange = { isChecked ->
                            // Call onToggle to update medication status
                            onToggle(isChecked)

                            // Trigger alarm logic if the switch is turned ON
                            if (isChecked) {
                                // Schedule alarm when turned on
                                scheduleAlarm(
                                    context = context,
                                    taskName = medication.name,
                                    taskTime = medication.time,
                                    alarmSoundUri = medication.alarmSound,
                                    days = medication.days
                                )
                            } else {
                                // Cancel alarm when turned off
                                cancelAlarm(context, medication.name, medication.days)
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

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Medication",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}



// Firebase Functions
fun deleteMedicationFromFirebase(database: DatabaseReference, medication: Medication) {
    val medicationId = medication.id
    if (medicationId.isNotEmpty()) {
        database.child(medicationId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MedicationScreen", "Medication deleted successfully")
            } else {
                Log.e("MedicationScreen", "Error deleting medication", task.exception)
            }
        }
    }
}

fun updateMedicationStatusInFirebase(database: DatabaseReference, medication: Medication, isOn: Boolean) {
    val medicationId = medication.id
    if (medicationId.isNotEmpty()) {
        database.child(medicationId).child("isOn").setValue(isOn).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MedicationScreen", "Medication status updated")
            } else {
                Log.e("MedicationScreen", "Error updating medication status", task.exception)
            }
        }
    }
}

// Helper function to parse time in a format that can be sorted
fun parseTime(time: String): Date {
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Time format: 12-hour AM/PM
    return format.parse(time) ?: Date()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MedicationScreenPreview() {
    val navController = rememberNavController()
    MedicationScreen(navController = navController)
}
