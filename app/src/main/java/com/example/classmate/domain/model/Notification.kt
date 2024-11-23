package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class Notification(
    var id: String = "",
    var aceptationDate: Timestamp = Timestamp.now(),
    var dateMonitoring : Timestamp =Timestamp.now(),
    var content:String ="",
    var subject: String = "",
    var studentId: String = "",
    var studentName: String = "",
    var monitorId: String = "",
    var monitorName: String = "",
    var type: Type_Notification = Type_Notification.DEFAULT
)
