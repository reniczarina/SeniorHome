package com.example.seniorshome

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationWithExpandableFAB(navController: NavController, currentScreen: String) {
    var isFabExpanded by remember { mutableStateOf(false) } // State for toggling additional FABs
    val context = LocalContext.current // Get the context for MediaPlayer

    // Shared MediaPlayer instance
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Function to play sound safely
    fun playSound(resId: Int) {
        // Stop and release the previous MediaPlayer if it exists
        mediaPlayer?.stop()
        mediaPlayer?.release()

        // Create a new MediaPlayer instance and start the sound
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            start()
            setOnCompletionListener {
                release()
                mediaPlayer = null // Set to null when playback completes
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color(0xFF004d00))
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Button
            IconButton(
                onClick = {
                    playSound(R.raw.dashboard) // Play home sound
                    navController.navigate("dashboard") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "dashboard") Color.White else Color(0xFF75D481)
                )
            }

            // Medicine Button
            IconButton(
                onClick = {
                    playSound(R.raw.medicine) // Play medicine sound
                    navController.navigate("medicine")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Medicine",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "medicine") Color.White else Color(0xFF75D481)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Spacer for FloatingActionButton

            // Notification Button
            IconButton(
                onClick = {
                    playSound(R.raw.notification) // Play notification sound
                    navController.navigate("notification")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "notification") Color.White else Color(0xFF75D481)
                )
            }

            // Profile Button
            IconButton(
                onClick = {
                    playSound(R.raw.profile) // Play profile sound
                    navController.navigate("profile")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "profile") Color.White else Color(0xFF75D481)
                )
            }
        }

        // Expandable FAB Options
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-100).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between items
        ) {
            AnimatedVisibility(visible = isFabExpanded) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            playSound(R.raw.addmedication) // Play Add Medication sound
                            navController.navigate("addmedicationscreen")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Medication",
                        tint = if (currentScreen == "medication_add") Color.White else Color(0xFF004d00),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = isFabExpanded) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            playSound(R.raw.addtask) // Play Add Task sound
                            navController.navigate("addtask")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddTask,
                        contentDescription = "Add Task",
                        tint = if (currentScreen == "task_add") Color.White else Color(0xFF004d00),
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        // Floating Action Button for Add
        FloatingActionButton(
            onClick = {
                playSound(R.raw.leftright) // Play FAB sound
                isFabExpanded = !isFabExpanded
            },
            containerColor = Color(0xFF75D481),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(72.dp)
                .offset(y = (-28).dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add",
                tint = Color(0xFF004d00),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}



