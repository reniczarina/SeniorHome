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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, controller: RegisterScreenController) {
    val context = LocalContext.current
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

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
        TextField(
            value = controller.email.value,
            onValueChange = { controller.email.value = it },
            label = { Text("Email", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFACDDB7),
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )
        // Password field with show/hide toggle
        TextField(
            value = controller.password.value,
            onValueChange = { controller.password.value = it },
            label = { Text("Password", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    val icon = if (passwordVisible.value) {
                        Icons.Default.Visibility // Show password icon
                    } else {
                        Icons.Default.VisibilityOff // Hide password icon
                    }
                    Icon(icon, contentDescription = "Toggle password visibility", tint = Color.Gray)
                }
            },
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFACDDB7),
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        // Confirm Password field with show/hide toggle
        TextField(
            value = controller.confirmPassword.value,
            onValueChange = { controller.confirmPassword.value = it },
            label = { Text("Confirm Password", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                    val icon = if (confirmPasswordVisible.value) {
                        Icons.Default.Visibility // Show password icon
                    } else {
                        Icons.Default.VisibilityOff // Hide password icon
                    }
                    Icon(icon, contentDescription = "Toggle confirm password visibility", tint = Color.Gray)
                }
            },
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFACDDB7),
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )


        controller.errorMessage.value?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                controller.registerUser {
                    Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
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
