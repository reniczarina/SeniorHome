package com.example.seniorshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons



@Composable
fun SeniorScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Seniors Home Logo
        Image(
            painter = painterResource(id = R.drawable.shlogo),  // Replace with your logo resource
            contentDescription = "Seniors Home Logo",
            modifier = Modifier.size(280.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = "SENIORS HOME",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF004d00)  // Dark Green
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Register Button with Material Icon
        Button(
            onClick = {
                navController.navigate("register")  // Navigate to Register screen
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00))
        ) {

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "REGISTER",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log In Button with Material Icon
        Button(
            onClick = {
                navController.navigate("login")  // Navigate to Login screen
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004d00))
        ) {

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LOG IN",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
