package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class Request(
    var id: String = "",
    var mode_class: Boolean = false,
    var type: String = "",
    var date: Timestamp = Timestamp.now(),
    var description: String = "",
    var place: String = "",
    var subject: String = "",
    var studentId: String = "",
    var studentName: String = "",
    var monitorId: String = "",
    var monitorName: String = ""
)
