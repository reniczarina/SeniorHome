package com.example.seniorshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorshome.ui.theme.SeniorsHomeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Seniors Home Icon
        Image(
            painter = painterResource(id = R.drawable.seniorslogo), // Replace with your seniorslogo resource
            contentDescription = "Seniors Home Icon",
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 24.dp)
        )

        // Title
        Text(
            text = "SENIORS HOME",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00), // Dark Green color
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Form Fields (Email/Username and Password)
        val emailUsername = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val loginError = remember { mutableStateOf("") }

        // Define the color for the TextFields
        val textFieldColor = Color(0xFFACDDB7) // Set text field color to ACDDB7

        TextField(
            value = emailUsername.value,
            onValueChange = { emailUsername.value = it },
            label = { Text("Email/Username") },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        // Display login error message
        if (loginError.value.isNotEmpty()) {
            Text(text = loginError.value, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 16.dp))
        }

        // Log In Button
        Button(
            onClick = {
                // Mock authentication logic
                if (emailUsername.value == "user@example.com" && password.value == "password") {
                    // Navigate to Dashboard
                    navController.navigate("dashboard") {
                        // Clear back stack to prevent going back to the login screen
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    // Show error message
                    loginError.value = "Invalid email/username or password."
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00)) // Dark Green
        ) {
            Text(text = "Log In", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password Text
        Text(
            text = "Forgot your password?",
            color = Color(0xFF004d00),
            modifier = Modifier.clickable {
                // Handle password recovery
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign up",
                color = Color(0xFF004d00),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        navController.navigate("register") // Navigate to register screen
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SeniorsHomeTheme {
        LoginScreen(navController = rememberNavController())
    }
}
