package com.example.seniorshome

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationWithExpandableFAB(navController: NavController, currentScreen: String) {
    var isFabExpanded by remember { mutableStateOf(false) } // State for toggling additional FABs

    Box(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
    ) {
        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp) // Set the height to fit the navigation bar size
                .background(Color(0xFF004d00)) // Background color
                .padding(horizontal = 16.dp), // Optional horizontal padding
            horizontalArrangement = Arrangement.spacedBy(24.dp), // Space between buttons
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Button
            IconButton(onClick = { navController.navigate("dashboard") }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "dashboard") Color.White else Color(0xFF75D481)
                )
            }
            // Medication Button
            IconButton(onClick = { navController.navigate("medicine") }) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Medicine",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "medicine") Color.White else Color(0xFF75D481)
                )
            }
            // Spacer for the Floating Button
            Spacer(modifier = androidx.compose.ui.Modifier.weight(1f)) // Adjusted weight spacer
            // Notification Button
            IconButton(onClick = { navController.navigate("notification") }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "notification") Color.White else Color(0xFF75D481)
                )
            }
            // Profile Button
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp),
                    tint = if (currentScreen == "profile") Color.White else Color(0xFF75D481)
                )
            }
        }


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
                        .clickable { navController.navigate("addmedicationscreen") },
                    contentAlignment = Alignment.Center // Ensures the icon is centered
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Medication",
                        tint = if (currentScreen == "medication_add") Color.White else Color(0xFF004d00),
                        modifier = Modifier.size(80.dp) // Adjust size as needed
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = isFabExpanded) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate("addtask") },
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




        var isFabClicked by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = {
                isFabExpanded = !isFabExpanded
                isFabClicked = !isFabClicked // Toggle the color state on click
            },
            containerColor = Color(0xFF75D481), // Background color
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(72.dp) // Adjust size as needed
                .offset(y = (-28).dp) // Raise above the navigation bar
        ) {
            Icon(
                imageVector = Icons.Rounded.Add, // Use Material 3 Add icon
                contentDescription = "Add",
                tint = if (isFabClicked) Color.White else Color(0xFF004d00), // Change color based on the click state
                modifier = Modifier.size(32.dp) // Adjust icon size as needed
            )
        }


    }
}
