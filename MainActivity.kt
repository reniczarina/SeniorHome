package com.example.seniorshome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seniorshome.ui.theme.SeniorsHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeniorsHomeTheme {
                // Navigation setup
                val navController = rememberNavController()

                // Scaffold with white background
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    // NavHost handles navigation between screens
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { SeniorsHomeUI(navController = navController) }
                        composable("register") { RegisterScreen(navController = navController) }
                        composable("login") { LoginScreen(navController = navController) } // Added LoginScreen
                    }
                }
            }
        }
    }
}

@Composable
fun SeniorsHomeUI(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Seniors Home Icon
        Image(
            painter = painterResource(id = R.drawable.seniorslogo),  // Replace with your logo resource
            contentDescription = "Seniors Home Icon",
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 24.dp)
        )

        // Title
        Text(
            text = "SENIORS HOME",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00)  // Dark Green
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Register Button with Icon
        Button(
            onClick = { navController.navigate("register") },  // Navigate to RegisterScreen
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = buttonColors(containerColor = Color(0xFF004d00))  // Dark Green
        ) {
            Icon(
                painter = painterResource(id = R.drawable.register),  // Replace with your icon resource
                contentDescription = "Register Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "REGISTER", color = Color.White, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button with Icon
        Button(
            onClick = { navController.navigate("login") },  // Navigate to LoginScreen
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp),
            colors = buttonColors(containerColor = Color(0xFF004d00))  // Dark Green
        ) {
            Icon(
                painter = painterResource(id = R.drawable.login),  // Replace with your icon resource
                contentDescription = "Log In Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "LOG IN", color = Color.White, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SeniorsHomeUIPreview() {
    SeniorsHomeTheme {
        SeniorsHomeUI(navController = rememberNavController())
    }
}
