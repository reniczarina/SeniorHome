package com.example.seniorshome

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedTimePickerExample(
    onConfirm: (String) -> Unit,  // Change this to accept a formatted time string
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    /** Determines whether the time picker is in dial mode or input mode */
    var showDial by remember { mutableStateOf(true) }

    /** The icon used for toggling between dial and input modes */
    val toggleIcon = if (showDial) {
        Icons.Filled.EditCalendar
    } else {
        Icons.Filled.AccessTime
    }

    AdvancedTimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = {
            // Convert time to 12-hour format with AM/PM
            val hourIn12HrFormat = if (timePickerState.hour >= 12) {
                if (timePickerState.hour > 12) timePickerState.hour - 12 else timePickerState.hour
            } else {
                if (timePickerState.hour == 0) 12 else timePickerState.hour
            }
            val amPm = if (timePickerState.hour >= 12) "PM" else "AM"

            // Format minutes to always display two digits
            val formattedMinutes = String.format("%02d", timePickerState.minute)

            // Combine hour, minutes, and AM/PM
            val formattedTime = "$hourIn12HrFormat:$formattedMinutes $amPm"

            // Pass the formatted time as a string
            onConfirm(formattedTime) // Send the formatted time string
        },
        toggle = {
            IconButton(onClick = { showDial = !showDial }) {
                Icon(
                    imageVector = toggleIcon,
                    contentDescription = "Switch Time Picker Mode"
                )
            }
        }
    ) {
        if (showDial) {
            // Dial-based Time Picker
            TimePicker(state = timePickerState)
        } else {
            // Input-based Time Picker
            TimeInput(state = timePickerState)
        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = Color.LightGray,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min) // Corrected to IntrinsicSize.Min
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = Color(0xFF004d00)
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog title
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )

                // Time picker content
                content()

                // Row for toggle and action buttons
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel",color = Color.Red) }
                    Button(onClick = onConfirm) { Text("OK",color = Color(0xFF004d00)) }
                }
            }
        }
    }
}

