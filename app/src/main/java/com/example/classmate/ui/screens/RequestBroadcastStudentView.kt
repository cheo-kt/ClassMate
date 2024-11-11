package com.example.classmate.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.google.gson.Gson

@Composable
fun RequestBroadcastStudentView(navController: NavController,requestBroadcast: String?) {

    val requestBroadcastObj: RequestBroadcast = Gson().fromJson(requestBroadcast, RequestBroadcast::class.java)

}