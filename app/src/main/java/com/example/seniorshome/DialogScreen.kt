package com.example.seniorshome

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DialogScreen(
    navController: NavController,
    onDismiss: () -> Unit,
    onTaskSelected: () -> Unit,
    onMedicationSelected: () -> Unit
) {
    var showTaskDialog by remember { mutableStateOf(false) }
    var showMedicationDialog by remember { mutableStateOf(false) }

    // Main content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Row with Task and Medication icons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Task Icon Button
            IconButton(
                onClick = { showTaskDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Task,
                    contentDescription = "Task Icon",
                    modifier = Modifier.size(48.dp),
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Medication Icon Button
            IconButton(
                onClick = { showMedicationDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Medication,
                    contentDescription = "Medication Icon",
                    modifier = Modifier.size(60.dp),
                    tint = Color.DarkGray
                )
            }
        }
    }

    // Dialog for Task
    if (showTaskDialog) {
        TaskConfirmationDialog(
            onConfirm = {
                showTaskDialog = false
                onTaskSelected()
                navController.navigate("addtask") // Navigate to AddTaskScreen
            },
            onDismiss = { showTaskDialog = false }
        )
    }

    // Dialog for Medication
    if (showMedicationDialog) {
        MedicationConfirmationDialog(
            onConfirm = {
                showMedicationDialog = false
                onMedicationSelected()
                navController.navigate("addmedicationscreen") // Navigate to MedicationScreen
            },
            onDismiss = { showMedicationDialog = false }
        )
    }
}

@Composable
fun TaskConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Task Confirmation",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Do you want to add a new task?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun MedicationConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Medication Confirmation",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("Do you want to add a new medication?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogScreen() {
    val navController = rememberNavController()

    DialogScreen(
        navController = navController,
        onDismiss = {},
        onTaskSelected = {},
        onMedicationSelected = {}
    )
}
