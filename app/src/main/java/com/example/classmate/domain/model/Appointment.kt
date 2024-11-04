package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class Appointment(
    var id: String = "",
    var mode_class: Boolean? = null,
    var type: String = "",
    var date: Timestamp? = null,
    var description: String = "",
    var place: String = "",
    var subject: String = "",
    var studentId: String = "",
    var studentName: String = "",
    var monitorId: String = "",
    var monitorName: String = "",
)


