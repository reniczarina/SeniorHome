package com.example.seniorshome

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

data class RegisterModel(
    val email: String,
    val password: String,
    val confirmPassword: String
) {
    suspend fun register(): Result {
        return try {
            if (password == confirmPassword) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .await() // This will suspend the execution until the task completes
                Result.Success
            } else {
                Result.Failure("Passwords do not match")
            }
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Registration failed")
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(val errorMessage: String) : Result()
    }
}