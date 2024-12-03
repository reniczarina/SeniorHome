package com.example.seniorshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween // Space between header, content, and footer
    ) {
        // Header with Logo and greeting
        Header()

        // Main Content Area (Floating Action Button with Plus Icon)
        CenterContent(navController)

        // Bottom Navigation Bar with Icons
        BottomNavigationBar(navController)
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.seniorslogo), // Replace with your logo
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Maayong Adlaw!",  // Greeting text in local dialect
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00)  // Dark Green color
        )
    }
}

@Composable
fun CenterContent(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating Action Button with a Plus Icon
        FloatingActionButton(
            onClick = {
                // Navigate to the Add Task screen or any other screen
                navController.navigate("add_task") // Replace with your destination
            },
            shape = CircleShape,
            containerColor = Color(0xFF004d00) // Dark Green color
        ) {
            Icon(
                painter = painterResource(id = R.drawable.addtask), // Replace with your plus icon
                contentDescription = "Add Task",
                tint = Color.White
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF004d00)) // Dark Green background for the bottom nav
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavigationIcon(iconRes = R.drawable.home, label = "Home") {
            navController.navigate("dashboard") // Replace with your route for Home
        }
        BottomNavigationIcon(iconRes = R.drawable.medicine, label = "Medicine") {
            navController.navigate("medicine") // Replace with your route for Medicine
        }
        BottomNavigationIcon(iconRes = R.drawable.message, label = "Message") {
            navController.navigate("message") // Replace with your route for Messages
        }
        BottomNavigationIcon(iconRes = R.drawable.notification, label = "Notification") {
            navController.navigate("notification") // Replace with your route for Notifications
        }
        BottomNavigationIcon(iconRes = R.drawable.profile, label = "Profile") {
            navController.navigate("profile") // Replace with your route for Profile
        }
    }
}

@Composable
fun BottomNavigationIcon(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(onClick = onClick) // Make the icon clickable
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp) // Icon size
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White // White text to contrast with the green background
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val navController = rememberNavController()
    DashboardScreen(navController = navController)
}
