package com.example.seniorshome

data class Task(
    val id: String = "",
    val name: String = "",
    val time: String = "",
    val days: List<String> = emptyList(), // Change to List<String>
    val isOn: Boolean = false,
    val alarmEnabled: Boolean = false,
    val alarmSound: String = "",
    val familyEmail: Boolean = false
)
