package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class Notification(
    var id: String = "",
    var date: Timestamp = Timestamp.now(),
    var content:String ="",
    var subject: String = "",
    var studentId: String = "",
    var studentName: String = "",
    var monitorId: String = "",
    var monitorName: String = ""
)
