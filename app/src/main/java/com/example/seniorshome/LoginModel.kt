package com.example.seniorshome

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginModel(private val auth: FirebaseAuth) {

    fun authenticateUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = (task.exception as? FirebaseAuthException)?.message ?: "Login failed"
                    onFailure(errorMessage)
                }
            }
    }

    fun sendPasswordReset(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = (task.exception as? FirebaseAuthException)?.message ?: "Error sending reset link"
                    onFailure(errorMessage)
                }
            }
    }
}
