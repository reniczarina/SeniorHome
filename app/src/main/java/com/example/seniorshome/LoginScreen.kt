package com.example.seniorshome

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun LoginScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val emailUsername = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val textFieldColor = Color(0xFFACDDB7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.shlogo),
            contentDescription = "Seniors Home Icon",
            modifier = Modifier.size(350.dp).padding(bottom = 24.dp)
        )

        Text(
            text = "WELCOME!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = emailUsername.value,
            onValueChange = { emailUsername.value = it },
            label = { Text("Email", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon", tint = Color.Gray) },
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                Icon(
                    imageVector = icon,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    tint = Color.Gray,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = textFieldColor,
                focusedIndicatorColor = Color(0xFF004d00),
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
        )

        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        Text(
            text = "Forgot your password?",
            color = Color(0xFF004d00),
            modifier = Modifier
                .clickable { showForgotPasswordDialog = true }
                .padding(bottom = 4.dp)  // Optional: adds space between text and line
                .background(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.small
                )
                .drawBehind {
                    drawLine(
                        color = Color(0xFF004d00),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f
                    )
                }
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (emailUsername.value.isEmpty() || password.value.isEmpty()) {
                    errorMessage = "Please fill in all fields."
                    return@Button
                }

                auth.signInWithEmailAndPassword(emailUsername.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val mediaPlayer = MediaPlayer.create(context, R.raw.welcome)
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { mediaPlayer.release() }

                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Invalid email or password. Please try again."
                        }
                    }
            }
            ,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00))
        ) {
            Text(text = "Log In", color = Color.White, fontSize = 20.sp)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Don't have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign up",
                color = Color(0xFF004d00),
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }

        if (showForgotPasswordDialog) {
            ForgotPasswordDialog(
                onDismiss = { showForgotPasswordDialog = false },
                onSendResetLink = { email ->
                    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                        showForgotPasswordDialog = false
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit, onSendResetLink: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Forgot Password Icon",
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier.size(28.dp).padding(end = 8.dp)
                )
                Text(
                    text = "Forgot Password",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
            }
        },
        text = {
            Column {
                Text("Enter your email to receive reset instructions.", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    shape = RoundedCornerShape(10.dp) // Set the corner radius to 10.dp
                )

            }
        },
        confirmButton = {
            Button(
                onClick = { onSendResetLink(email) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004d00)
                )
            ) {
                Text("Send", color = Color.Gray) // Set the text color to black
            }

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = Color(0xFF424D43)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SeniorsHomeTheme {
        LoginScreen(navController = rememberNavController())
    }
}
