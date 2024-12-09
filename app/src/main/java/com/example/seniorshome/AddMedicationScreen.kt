package com.example.seniorshome

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.provider.MediaStore
import android.app.Activity
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext

// Request code for picking an audio file
private const val PICK_AUDIO_REQUEST_CODE = 1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(navController: NavController) {
    val context = LocalContext.current
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("medication")

    var medicationName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var alarmSoundEnabled by remember { mutableStateOf(false) }
    var selectedAlarmSound by remember { mutableStateOf("") }
    var familyNotificationEnabled by remember { mutableStateOf(false) }
    var familyEmail by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(mutableListOf<String>()) }
    var familyPhoneNumber by remember { mutableStateOf("") }

    // Dialog states
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    // Function to handle time picker confirmation
    // Function to handle time picker confirmation
    val onTimeConfirm: (String) -> Unit = { formattedTime ->
        // Use the formatted time (String) received from the time picker
        selectedTime = formattedTime
        // Dismiss the dialog after selection
        showTimePickerDialog = false
    }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Function to play sound
    fun playSound(resId: Int) {
        try {
            // Release any previous instance of MediaPlayer if it exists
            mediaPlayer?.stop()
            mediaPlayer?.release()

            // Create a new MediaPlayer and start the sound
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                start()
                setOnCompletionListener {
                    release()
                    mediaPlayer = null // Set to null after playback is complete
                    Log.d("Sound", "Playback completed and MediaPlayer released.")
                }
            }
            Log.d("Sound", "Sound played: $resId")
        } catch (e: Exception) {
            Log.e("Sound", "Error playing sound: ${e.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Exit",
                    tint = Color.Red
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add your Medication",
                fontSize = 24.sp,
                color = Color(0xFF004d00),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    playSound(R.raw.dashboard) // Play sound
                    navController.navigate("dashboard") // Navigate to dashboard
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF004d00)
                )
            }
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display the selected time
            Text("Selected Time: $selectedTime")

            // Show the Time Picker Dialog
            if (showTimePickerDialog) {
                AdvancedTimePickerExample(
                    onConfirm = onTimeConfirm, // This function is now of type (String) -> Unit
                    onDismiss = { showTimePickerDialog = false }
                )
            }
            Button(
                onClick = { showTimePickerDialog = true },
                modifier = Modifier
                    .width(145.dp)  // Set the width
                    .height(55.dp)  // Set the height
                    .padding(vertical = 8.dp)
                    .background(Color(0xFF0B4413), shape = MaterialTheme.shapes.medium)  // Button background color and corner radius
                    .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium),  // Inner shadow effect
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B4413))  // Button color
            ) {
                Text(
                    text = "Select Time",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,  // ExtraBold font weight
                )
            }

            OutlinedTextField(
                value = medicationName,
                onValueChange = { medicationName = it },
                label = { Text("Medication Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    containerColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontStyle = FontStyle.Italic)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = alarmSoundEnabled,
                    onCheckedChange = { isChecked ->
                        alarmSoundEnabled = isChecked
                        if (isChecked) {
                            playSound(R.raw.enablealarmsoundmedication) // Replace with the correct resource ID
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Enable Alarm Sound")
            }
            if (alarmSoundEnabled) {
                // Add Alarm sound selector for medications
                AlarmSoundSelector(selectedAlarmSound = selectedAlarmSound) { sound ->
                    selectedAlarmSound = sound
                }
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = familyNotificationEnabled,
                        onCheckedChange = { isChecked ->
                            familyNotificationEnabled = isChecked
                            if (isChecked) {
                                playSound(R.raw.notifymedicationmissed) // Replace with the correct resource ID
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Notify Family if Missed")
                }
                if (familyNotificationEnabled) {
                    OutlinedTextField(
                        value = familyEmail,
                        onValueChange = { familyEmail = it },
                        label = { Text("Family's Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            containerColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontStyle = FontStyle.Italic)
                    )
                }
            }

            Text(text = "Set Schedule", fontWeight = FontWeight.Bold)
            MedicationSelectDaysDropdown(selectedDays = selectedDays)
        }

        // Save Button
        Button(
            onClick = {
                playSound(R.raw.medicationconfirmation) // Replace with the correct resource ID
                showSaveDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B4413))
        ) {
            Text(text = "Save", color = Color.White, fontStyle = FontStyle.Italic)
        }
    }

    // Save Confirmation Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = {
                Text(
                    "Are you sure you want to save?",
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val medication = Medication(
                            name = medicationName,
                            time = selectedTime,
                            days = selectedDays,  // Pass the list of selected days directly
                            isOn = false,
                            alarmEnabled = alarmSoundEnabled,
                            alarmSound = selectedAlarmSound,
                            familyPhoneNumber = if (familyNotificationEnabled) familyPhoneNumber else ""
                        )

                        saveMedicationToFirebase(database, medication)

                        // Schedule the alarm after saving the task to Firebase
                        scheduleAlarm(
                            context = context,
                            taskName = medicationName,
                            taskTime = selectedTime,
                            alarmSoundUri = selectedAlarmSound,
                            days = selectedDays, // Pass the list of selected days
                            phoneNumber = familyPhoneNumber
                        )

                        navController.navigate("medicine")
                        showSaveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B4413))
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = Color.Black)
                }
            },
            containerColor = Color.White,
            modifier = Modifier.size(363.dp, 170.dp)
        )
    }
}


fun saveMedicationToFirebase(database: DatabaseReference, medication: Medication) {
    val medicationId = database.push().key ?: return
    val medicationWithId = medication.copy(id = medicationId)

    database.child(medicationId).setValue(medicationWithId).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Log.d("AddMedicationScreen", "Medication saved: $medicationWithId")
        } else {
            Log.e("AddMedicationScreen", "Error saving medication", task.exception)
        }
    }
}

@Composable
fun AlarmSoundSelector(selectedAlarmSound: String, onSoundSelected: (String) -> Unit) {
    val alarmSounds = listOf("Beep", "Chime", "Buzz", "Ding", "Nature Sounds", "Pick Audio")
    var expanded by remember { mutableStateOf(false) }

    // Variable to store the selected audio URI
    var selectedAudioUri by remember { mutableStateOf<String?>(null) }

    // Activity result launcher to pick audio file
    val getAudioUri = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val audioUri = result.data?.data // Get selected audio URI
                audioUri?.let {
                    selectedAudioUri = it.toString() // Store the URI
                    onSoundSelected(it.toString()) // Notify the parent composable
                }
            }
        }
    )

    Column {
        TextButton(onClick = { expanded = true }) {
            Text(
                if (selectedAlarmSound.isEmpty()) "Select Alarm Sound" else selectedAlarmSound,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF0B4413)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            alarmSounds.forEach { sound ->
                DropdownMenuItem(
                    text = { Text(sound) },
                    onClick = {
                        if (sound == "Pick Audio") {
                            // Launch the intent to pick an audio file
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                            getAudioUri.launch(intent)
                        } else {
                            // For predefined sounds
                            onSoundSelected(sound)
                            expanded = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MedicationSelectDaysDropdown(selectedDays: MutableList<String>) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Everyday")
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Function to play sound
    fun playSound(resId: Int) {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()

            mediaPlayer = MediaPlayer.create(context, resId).apply {
                start()
                setOnCompletionListener {
                    release()
                    mediaPlayer = null // Set to null after playback completes
                }
            }
        } catch (e: Exception) {
            Log.e("Sound", "Error playing sound: ${e.message}")
        }
    }

    Column {
        TextButton(
            onClick = {
                expanded = true
                playSound(R.raw.selectdaysmedication) // Replace with the correct resource ID
            }
        ) {
            Text(
                text = "Select Days",
                fontStyle = FontStyle.Italic,
                color = Color(0xFF0B4413)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            daysOfWeek.forEach { day ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedDays.contains(day),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedDays.add(day)
                                    } else {
                                        selectedDays.remove(day)
                                    }
                                }
                            )
                            Text(text = day)
                        }
                    },
                    onClick = { /* No action needed here */ }
                )
            }
        }

        if (selectedDays.isNotEmpty()) {
            Text(
                text = "Selected Days: ${selectedDays.joinToString()}",
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B4413)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddMedicationScreen() {
    AddMedicationScreen(navController = rememberNavController())
}
