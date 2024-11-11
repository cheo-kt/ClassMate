package com.example.classmate.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.RequestBroadcast
import com.google.gson.Gson

@Composable
fun AppoimentStudentScreen(navController: NavController,appointment: String?) {
    val appointmentObj: Appointment = Gson().fromJson(appointment, Appointment::class.java)
}