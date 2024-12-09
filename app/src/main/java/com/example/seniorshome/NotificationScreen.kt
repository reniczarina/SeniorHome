package com.example.seniorshome

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow


class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext
    private val _notifications = MutableStateFlow<List<String>>(loadNotifications(context))
    val notifications: StateFlow<List<String>> = _notifications

    fun addNotification(notification: String) {
        _notifications.value += notification
        saveNotifications(notification, context)
    }

    fun deleteNotification(notification: String) {
        _notifications.value -= notification
        removeNotification(notification, context)
    }

    private fun loadNotifications(context: Context): List<String> {
        val sharedPreferences = context.getSharedPreferences("Notifications", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("notifications", emptySet())?.toList() ?: emptyList()
    }

    private fun saveNotifications(notification: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("Notifications", Context.MODE_PRIVATE)
        val notifications = sharedPreferences.getStringSet("notifications", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        notifications.add(notification)

        sharedPreferences.edit().putStringSet("notifications", notifications).apply()
    }

    private fun removeNotification(notification: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("Notifications", Context.MODE_PRIVATE)
        val notifications = sharedPreferences.getStringSet("notifications", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        notifications.remove(notification)

        sharedPreferences.edit().putStringSet("notifications", notifications).apply()
    }
}


@Composable
fun NotificationScreen(navController: NavController, viewModel: NotificationViewModel = viewModel()) {
    val notifications by viewModel.notifications.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var notificationToDelete by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        NotificationHeader()

        // Notifications list
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
            title = { Text("Delete this notification?", color = Color.Black) },
            confirmButton = {
                Button(
                    onClick = {
                        val mediaPlayer = MediaPlayer.create(context, R.raw.deletenotification)
                        mediaPlayer.start()
                        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
                        viewModel.deleteNotification(notificationToDelete!!)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel", color = Color.Black) } },
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
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 5.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

@Composable
fun NotificationList(notifications: List<String>, onDeleteNotification: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        notifications.forEach { notification ->
            NotificationItem(notification = notification, onDelete = { onDeleteNotification(notification) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun NotificationItem(notification: String, onDelete: () -> Unit) {
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
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
