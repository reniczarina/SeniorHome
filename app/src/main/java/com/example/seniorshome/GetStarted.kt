package com.example.seniorshome

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.seniorshome.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun GetStartedScreen(navController: NavController) {
    // Firebase Authentication instance
    val firebaseAuth = FirebaseAuth.getInstance()

    // Remember the MediaPlayer and control its state
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.welcometosh) }
    var isSoundPlayed by remember { mutableStateOf(false) }  // Track if sound has been played

    // Sound effect when user clicks "Get Started"
    LaunchedEffect(isSoundPlayed) {
        if (isSoundPlayed) {
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                it.release() // Release after completion
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo at the top center
        Image(
            painter = painterResource(id = R.drawable.shlogo),  // Your logo resource
            contentDescription = "Seniors Home Logo",
            modifier = Modifier.size(320.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable content with app description
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "ABOUT THE APP:",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004d00),  // Dark Green
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,  // Center the title text
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Box for the description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)  // Adjusted height for better readability
                    .border(
                        width = 2.dp,  // Border thickness
                        color = Color(0xFF004d00),  // Green border color
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Our app is designed to empower seniors in managing their daily tasks and medication schedules with ease. " +
                            "With a user-friendly interface, seniors can easily input their tasks and medication details, including schedules and timing. " +
                            "The app sends timely reminders and alarms to ensure that nothing is overlooked. " +
                            "If a task or medication is missed, the app will automatically notify designated family members, enabling them to provide gentle reminders and support. " +
                            "Our goal is to promote independence and peace of mind, making it easier for seniors to stay organized and on track with their health and daily activities.",
                    fontSize = 14.sp,  // Adjusted font size for better fit
                    color = Color.Black,  // Text color set to black for readability
                    textAlign = TextAlign.Justify,  // Justified text to fit better within the box
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Get Started Button
        Button(
            onClick = {
                // Play sound effect when the Get Started button is clicked
                isSoundPlayed = true  // Trigger sound playback

                // Check if user is authenticated
                if (firebaseAuth.currentUser != null) {
                    // Navigate to SeniorScreen if the user is authenticated
                    navController.navigate("dashboard")
                } else {
                    // Navigate to LoginScreen if the user is not authenticated
                    navController.navigate("senior_screen")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00)),  // Dark Green
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "GET STARTED", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
