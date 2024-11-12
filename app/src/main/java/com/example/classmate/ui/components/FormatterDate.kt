package com.example.classmate.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun FormatterDate(timestamp: Timestamp):String {
    val instant = timestamp.toInstant()
    val now = Instant.now()
    val minutes = ChronoUnit.MINUTES.between(instant, now)
    val hours = ChronoUnit.HOURS.between(instant, now)
    val days = ChronoUnit.DAYS.between(instant, now)
    val displayText = when {
            minutes < 60 -> {
            "$minutes m"
        }
            days < 1 -> {
            "$hours h"
        }
        else -> {
            "$days d"
        }
    }
    return displayText
}