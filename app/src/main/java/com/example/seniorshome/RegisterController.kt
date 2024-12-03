package com.example.seniorshome

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterController {
    suspend fun registerUser(registerModel: RegisterModel): RegisterModel.Result {
        return withContext(Dispatchers.IO) {
            registerModel.register()
        }
    }
}