package com.example.seniorshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: "No email available"
    val name = currentUser?.displayName ?: "User"

    val openLogoutDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(navController)
            ProfileForm(navController, name, email, openLogoutDialog, snackbarHostState, coroutineScope)
        }

        if (openLogoutDialog.value) {
            LogoutConfirmationDialog(
                onDismiss = { openLogoutDialog.value = false },
                onConfirm = {
                    auth.signOut()
                    unauthenticateUser(navController)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Logout successful",
                            duration = SnackbarDuration.Indefinite
                        )
                        delay(15000L) // Wait for 15 seconds before dismissing
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                    openLogoutDialog.value = false
                }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

fun unauthenticateUser(navController: NavController) {
    // Navigate back to the login screen
    navController.navigate("login") {
        popUpTo("login") { inclusive = true }
    }
}

@Composable
fun ProfileHeader(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            modifier = Modifier
                .size(32.dp)
                .clickable { navController.navigate("dashboard") },
            tint = Color(android.graphics.Color.parseColor("#0B4413")) // Use tint instead of colorFilter
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.Gray, thickness = 1.dp)

        Text(
            text = "Profile",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B4413)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileForm(
    navController: NavController,
    name: String,
    email: String,
    openLogoutDialog: MutableState<Boolean>,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profileg),
            contentDescription = "Profile Icon",
            modifier = Modifier.size(175.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {},
            label = { Text("Name", color = Color(0xFF0B4413)) },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Person Icon", tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF0B4413),
                unfocusedLabelColor = Color.Gray,
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.Gray),
            readOnly = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email Address", color = Color(0xFF0B4413)) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon", tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF0B4413),
                unfocusedLabelColor = Color.Gray,
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.Gray),
            readOnly = true
        )

        var password by remember { mutableStateOf("SeniorsHome") }
        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = if (passwordVisible) password else "***********",
            onValueChange = {},
            label = { Text("Password", color = Color(0xFF0B4413)) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Lock Icon", tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFF0B4413),
                unfocusedLabelColor = Color.Gray, 
            ),
            textStyle = LocalTextStyle.current.copy(color = Color.Gray),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        tint = Color.Gray
                    )
                }
            },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { openLogoutDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#0B4413")))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Logout, "Logout Icon", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "LOG OUT", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Log out of your account?",
                fontWeight = FontWeight.Bold,
                color = Color.Black // Title text set to Black
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray) // LOG OUT button background set to Red
            ) {
                Text("LOG OUT", color = Color.Black) // LOG OUT button text set to White
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("CANCEL", color = Color.Black) // CANCEL button text set to Black
            }
        },
        containerColor = Color.White, // Dialog background set to White
        modifier = Modifier.size(363.dp, 150.dp) // Dialog size: WIDTH = 363, HEIGHT = 100
    )
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
