package com.example.seniorshome

import com.google.firebase.auth.FirebaseAuth

class LoginModel(private val auth: FirebaseAuth) {

    // Sign in the user with email and password
    fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)  // Success
                } else {
                    callback(false, task.exception?.message)  // Error
                }
            }
    }

    // Send password reset email
    fun sendPasswordResetEmail(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)  // Success
                } else {
                    callback(false, task.exception?.message)  // Error
                }
            }
    }
}
