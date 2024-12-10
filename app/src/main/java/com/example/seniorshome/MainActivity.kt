package com.example.seniorshome

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seniorshome.ui.theme.SeniorsHomeTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var requestSmsPermissionLauncher: ActivityResultLauncher<String>
    private var isSmsPermissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        // Initialize the permission launcher for requesting SMS permission
        requestSmsPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            isSmsPermissionGranted = isGranted
        }

        // Request SMS permission if not granted
        if (!hasSmsPermission()) {
            requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        } else {
            isSmsPermissionGranted = true
        }

        setContent {
            SeniorsHomeTheme {
                val navController = rememberNavController()
                val registerScreenController = RegisterScreenController(auth)


                NavHost(navController = navController, startDestination = "splash_screen") {
                    composable("splash_screen") { SplashScreen(navController) }
                    composable("get_started") { GetStartedScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController, registerScreenController) }
                    composable("senior_screen") { SeniorScreen(navController) }
                    composable("dashboard") { TaskPage().DashboardScreen(navController) }
                    composable("addtask") { AddTask().AddTaskScreen(navController) }
                    composable("medicine") { MedicationPage().MedicationScreen(navController) }
                    composable("notification") { NotificationPage().NotificationScreen(navController) }
                    composable("profile") { ProfilePage().ProfileScreen(navController) }
                    composable("addmedicationscreen") { AddMedication().AddMedicationScreen(navController) }
                    composable("dialog_screen") { DialogScreen(navController, onDismiss = {}, onTaskSelected = {}, onMedicationSelected = {}) }
                }
            }
        }
    }

    // Check if permission is granted
    private fun hasSmsPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // State for progress
    var progress by remember { mutableStateOf(0f) }

    // Show the logo image for 3 seconds and update progress
    LaunchedEffect(Unit) {
        val duration = 1000L // Total duration in milliseconds (3 seconds)
        val steps = 100       // Number of steps to update progress
        val stepDuration = duration / steps  // Time per step

        for (i in 0..steps) { // Increment progress in steps
            delay(stepDuration) // Delay between each increment
            progress = i / 100f
        }

        // Navigate to the next screen after progress completes
        navController.navigate("get_started") {
            popUpTo("splash_screen") { inclusive = true } // Clear splash screen from back stack
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Center the content
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.shlogo), // Your logo image
                contentDescription = "Logo",
                modifier = Modifier.size(400.dp) // Adjust the logo size as needed
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between logo and progress indicator

            // Linear Progress Indicator
            LinearProgressIndicator(
                progress = {
                    progress // Set the progress state
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Make it 80% of the screen width
                    .height(4.dp), // Adjust height as needed
                color = Color(0xFFF6F6F6), // Set the color of the progress indicator
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController()
    SplashScreen(navController)
}
  