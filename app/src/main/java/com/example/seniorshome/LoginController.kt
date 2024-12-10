package com.example.seniorshome

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginController(private val view: LoginView, private val auth: FirebaseAuth) {

    private val model = LoginModel(auth)

    // Handle user login
    fun handleLogin(email: String, password: String, navController: NavController, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Please fill in all fields.")
            return
        }

        model.signIn(email, password) { isSuccess, errorMessage ->
            if (isSuccess) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                view.showErrorMessage(errorMessage ?: "Invalid login attempt.")
            }
        }
    }

    // Handle password reset
    fun handlePasswordReset(email: String, context: Context) {
        model.sendPasswordResetEmail(email) { isSuccess, errorMessage ->
            if (isSuccess) {
                Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: ${errorMessage ?: "Failed to send reset link."}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
