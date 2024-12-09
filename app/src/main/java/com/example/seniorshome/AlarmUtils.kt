package com.example.seniorshome

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*

@SuppressLint("ScheduleExactAlarm")
fun scheduleAlarm(
    context: Context,
    taskName: String,
    taskTime: String,
    alarmSoundUri: String,
    days: List<String>, // List of selected days
    phoneNumber: String // Phone number to notify
) {
    try {
        // Parse the time string to extract hour and minute
        val timeParts = taskTime.split(" ") // Split into time and AM/PM
        val time = timeParts[0] // Time in "HH:mm" format
        val amPm = timeParts[1] // "AM" or "PM"

        val hourMinute = time.split(":") // Split time into hour and minute
        var hour = hourMinute[0].toInt()
        val minute = hourMinute[1].toInt()

        // Convert to 24-hour format
        if (amPm == "AM" && hour == 12) hour = 0 // Midnight case
        if (amPm == "PM" && hour != 12) hour += 12

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("taskName", taskName)
            putExtra("taskTime", taskTime)
            putExtra("days", days.toTypedArray())
            putExtra("alarmSoundUri", alarmSoundUri)
            putExtra("phoneNumber", phoneNumber)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Helper function to schedule for a specific day
        fun scheduleForDay(day: Int) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)

                // Adjust to the specific day
                while (get(Calendar.DAY_OF_WEEK) != day) {
                    add(Calendar.DAY_OF_WEEK, 1)
                }
            }

            val requestCode = (taskName + day).hashCode() // Unique request code
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule the alarm
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

            Log.d("AlarmUtils", "Alarm scheduled for $taskName at $taskTime on day $day")
        }

        // Handle each selected day
        days.forEach { day ->
            when (day) {
                "Mon" -> scheduleForDay(Calendar.MONDAY)
                "Tue" -> scheduleForDay(Calendar.TUESDAY)
                "Wed" -> scheduleForDay(Calendar.WEDNESDAY)
                "Thu" -> scheduleForDay(Calendar.THURSDAY)
                "Fri" -> scheduleForDay(Calendar.FRIDAY)
                "Sat" -> scheduleForDay(Calendar.SATURDAY)
                "Sun" -> scheduleForDay(Calendar.SUNDAY)
                "Everyday" -> {
                    // Schedule for all days
                    listOf(
                        Calendar.MONDAY,
                        Calendar.TUESDAY,
                        Calendar.WEDNESDAY,
                        Calendar.THURSDAY,
                        Calendar.FRIDAY,
                        Calendar.SATURDAY,
                        Calendar.SUNDAY
                    ).forEach { scheduleForDay(it) }
                }
                else -> throw IllegalArgumentException("Invalid day: $day")
            }
        }
    } catch (e: Exception) {
        Log.e("AlarmUtils", "Error scheduling alarm: ${e.message}")
        e.printStackTrace()
    }
}

fun cancelAlarm(context: Context, taskName: String, days: List<String>) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("taskName", taskName)
    }

    // Cancel alarms for each day
    days.forEach { day ->
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            day.hashCode(),  // Use a unique request code for each day
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        Log.d("AlarmUtils", "Alarm canceled for $taskName on $day")
    }
}
