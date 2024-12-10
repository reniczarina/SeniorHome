package com.example.seniorshome

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class RegisterScreenModel(private val auth: FirebaseAuth) {

    suspend fun registerUser(user: User): Boolean {
        if (user.password != user.confirmPassword) {
            throw IllegalArgumentException("Passwords do not match")
        }

        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            true
        } catch (e: Exception) {
            throw e
        }
    }
}
