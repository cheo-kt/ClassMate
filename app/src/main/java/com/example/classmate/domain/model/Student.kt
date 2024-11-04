package com.example.classmate.domain.model

data class Student (
    var id: String = "",
    var name: String = "",
    var lastname: String = "",
    var phone: String = "",
    var email: String = "",
    var photo: String = "",
    var description: String = "",
    var appointments: List<Appointment> = listOf(),
    var notifications: List<Notification> = listOf(),
    var requests: List<Request> = listOf()
)