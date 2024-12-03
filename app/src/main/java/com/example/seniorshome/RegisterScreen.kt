package com.example.seniorshome

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.seniorshome.ui.theme.SeniorsHomeTheme
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.shlogo),
            contentDescription = "Seniors Home Icon",
            modifier = Modifier
                .size(350.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Create an Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val confirmPassword = remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val textFieldColor = Color(0xFFACDDB7)
        val iconTintColor = Color.Gray

        // Show/hide password states
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }


        // Email Field with Icon
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email Address", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = iconTintColor)
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        // Password Field with show/hide icon
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "Password Icon", tint = iconTintColor)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        // Confirm Password Field with show/hide icon
        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "Confirm Password Icon", tint = iconTintColor)
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide Confirm Password" else "Show Confirm Password",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        // Display error message if there is an error
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (password.value == confirmPassword.value) {
                    auth.createUserWithEmailAndPassword(email.value, password.value)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("login")
                            } else {
                                errorMessage = task.exception?.message ?: "Registration failed"
                            }
                        }
                } else {
                    errorMessage = "Passwords do not match"
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00))
        ) {
            Text(text = "Register", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Do you have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Log in",
                color = Color(0xFF004d00),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { navController.navigate("login") }
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
