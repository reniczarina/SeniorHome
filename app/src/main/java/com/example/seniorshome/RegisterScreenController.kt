package com.example.seniorshome

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterScreenController(private val auth: FirebaseAuth) : ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)

    private val userRepository = RegisterScreenModel(auth)

    fun registerUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val user = User(email.value, password.value, confirmPassword.value)
                if (user.password == user.confirmPassword) {
                    val success = userRepository.registerUser(user)
                    if (success) {
                        onSuccess()
                    }
                } else {
                    errorMessage.value = "Passwords do not match"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}
