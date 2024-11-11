package com.example.classmate.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class Monitor(
    var id: String = "",
    var name: String = "",
    var lastname: String = "",
    var phone: String = "",
    var subjects: List<MonitorSubject> = emptyList(),
    var email: String = "",
    var description: String ="",
    var photoUrl: String = "",
    var rating: Int = 0,
    var appointments: List<Appointment> = listOf(),
    var notifications: List<Notification> = listOf(),
    var requests: List<Request> = listOf()
)