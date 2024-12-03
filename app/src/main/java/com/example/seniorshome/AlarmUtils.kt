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
    days: List<String> // Change this to a List of days (Mon, Tue, etc.)
) {
    try {
        // Convert the time string to a Calendar object
        val timeParts = taskTime.split(" ")  // Split into time and AM/PM
        val time = timeParts[0] // Time in "HH:mm" format
        val amPm = timeParts[1] // "AM" or "PM"

        val hourMinute = time.split(":") // Split time into hour and minute
        var hour = hourMinute[0].toInt()
        val minute = hourMinute[1].toInt()

        // Adjust hour based on AM/PM
        if (amPm == "AM" && hour == 12) {
            hour = 0  // Midnight case (12:00 AM)
        } else if (amPm == "PM" && hour != 12) {
            hour += 12  // Convert PM hour to 24-hour format
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("taskName", taskName) // Pass the task name
            putExtra("taskTime", taskTime) // Pass the task time
            putExtra("days", days.toTypedArray()) // Pass the days list as an array (since Intent can only pass arrays, not lists directly)
            putExtra("alarmSoundUri", alarmSoundUri) // Pass the alarm sound URI
        }


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Loop through the selected days and set the alarm for each day
        days.forEach { day ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)

                // Adjust the calendar to match the selected day
                when (day) {
                    "Mon" -> set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    "Tue" -> set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
                    "Wed" -> set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
                    "Thu" -> set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
                    "Fri" -> set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                    "Sat" -> set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                    "Sun" -> set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                    "Everyday" -> set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)  // Default to Monday
                    else -> throw IllegalArgumentException("Invalid day: $day")
                }
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                day.hashCode(),  // Use a unique request code for each day
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // For exact alarm scheduling with Android 12+
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

            Log.d("AlarmUtils", "Alarm scheduled for $taskName at $taskTime on $day")
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
