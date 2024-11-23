package com.example.classmate.ui.components

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.Composable
import com.google.firebase.Timestamp
import java.util.Locale
@Composable
fun FormatterHour(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}