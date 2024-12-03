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
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)  // Background color set to white
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Seniors Home Icon
        Image(
            painter = painterResource(id = R.drawable.seniorslogo),  // Replace with your seniorslogo resource
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
            color = Color(0xFF004d00),  // Dark Green color
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Form Fields (Username, Email, Password, Confirm Password)
        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val confirmPassword = remember { mutableStateOf("") }

        // Define the color for the TextFields
        val textFieldColor = Color(0xFFACDDB7) // Set text field color to ACDDB7

        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor, // Apply ACDDB7 color
                focusedIndicatorColor = Color(0xFF004d00), // Optional: change the indicator color to match the theme
                unfocusedIndicatorColor = Color.Transparent, // Make it transparent when not focused
                cursorColor = Color.White, // Set the cursor color to white
                focusedLabelColor = Color.White, // Set the label color when focused to white
                unfocusedLabelColor = Color.White // Set the label color when unfocused to white
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set the text color to white
        )

        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White, // Set the cursor color to white
                focusedLabelColor = Color.White, // Set the label color when focused to white
                unfocusedLabelColor = Color.White // Set the label color when unfocused to white
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set the text color to white
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White, // Set the cursor color to white
                focusedLabelColor = Color.White, // Set the label color when focused to white
                unfocusedLabelColor = Color.White // Set the label color when unfocused to white
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set the text color to white
        )

        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password") },
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
                cursorColor = Color.White, // Set the cursor color to white
                focusedLabelColor = Color.White, // Set the label color when focused to white
                unfocusedLabelColor = Color.White // Set the label color when unfocused to white
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black) // Set the text color to white
        )

        // Register Button
        Button(
            onClick = {
                // Handle register button click
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00))  // Dark Green
        ) {
            Text(text = "Register", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log In Text
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Do you have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Log in",
                color = Color(0xFF004d00),  // Dark Green
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        navController.navigate("login")  // Navigate back to login screen
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SeniorsHomeTheme {
        RegisterScreen(navController = rememberNavController())
    }
}
