package com.example.classmate.domain.model

data class Monitor(
    var id: String = "",
    var name: String = "",
    var lastname: String = "",
    var phone: String = "",
    var subjects: List<MonitorSubject> = emptyList(),
    var email: String = "",
    var description: String ="",
    var photoUrl: String = "",
    var rating: Double = 0.0,
)