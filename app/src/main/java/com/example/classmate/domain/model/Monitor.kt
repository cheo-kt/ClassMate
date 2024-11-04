package com.example.classmate.domain.model

data class Monitor(
    var id: String = "",
    var name: String = "",
    var lastname: String = "",
    var phone: String = "",
    var subjects: List<Subject> = ArrayList<Subject>(),
    var email: String = "",
    var description: String ="",
    var photoUrl: String = "",
    var rating: Int = 0,
    var appointments: List<Appointment> = listOf(),
    var notifications: List<Notification> = listOf(),
    var requests: List<Request> = listOf()



)