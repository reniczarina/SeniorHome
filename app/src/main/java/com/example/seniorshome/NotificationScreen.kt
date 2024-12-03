package com.example.seniorshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel to manage notifications
class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = _notifications

    fun addNotification(notification: String) {
        _notifications.value = _notifications.value + notification
    }

    fun deleteNotification(notification: String) {
        _notifications.value = _notifications.value - notification
    }
}

@Composable
fun NotificationScreen(navController: NavController, viewModel: NotificationViewModel = viewModel()) {
    val notifications by viewModel.notifications.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var notificationToDelete by remember { mutableStateOf<String?>(null) }

    SimulateNotification(viewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header with Logo and greeting
        NotificationHeader()

        // Notifications title and scrollable notification list
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            NotificationList(
                notifications = notifications,
                onDeleteNotification = { notification ->
                    notificationToDelete = notification
                    showDeleteDialog = true
                }
            )
        }

        // Bottom Navigation
        BottomNavigationWithExpandableFAB(navController = navController, currentScreen = "notification")
    }

    // Delete confirmation dialog
    if (showDeleteDialog && notificationToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete this notification?",
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteNotification(notificationToDelete!!)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel", color = Color.Black)
                }
            },
            containerColor = Color.White,
            modifier = Modifier.size(363.dp, 150.dp)
        )
    }
}

@Composable
fun NotificationHeader() {
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
                painter = painterResource(id = R.drawable.shlogo),
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
fun NotificationList(
    notifications: List<String>,
    onDeleteNotification: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        notifications.forEach { notification ->
            NotificationItem(
                notification = notification,
                onDelete = { onDeleteNotification(notification) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NotificationItem(
    notification: String,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF004d00), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = notification,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Notification",
                    tint = Color.Red
                )
            }
        }
    }
}

// Simulate real-time notifications
@Composable
fun SimulateNotification(viewModel: NotificationViewModel) {
    LaunchedEffect(Unit) {
        val sampleNotifications = listOf(
            "Time to go for a morning walk.",
            "Remember to take your afternoon medication.",
            "Hydrate! Drink water now.",
            "Your weekly yoga class starts in 30 minutes.",
            "Don't forget your health check-up tomorrow."
        )
        sampleNotifications.forEachIndexed { index, message ->
            delay((index + 1) * 5000L) // Delay in milliseconds
            viewModel.addNotification(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    val navController = rememberNavController()
    NotificationScreen(navController = navController)
}
