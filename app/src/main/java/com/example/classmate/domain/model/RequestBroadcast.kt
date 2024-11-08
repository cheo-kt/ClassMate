package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class RequestBroadcast(var id: String = "",
                            var mode_class: String = "",
                            var type: String = "",
                            var date: Timestamp = Timestamp.now(),
                            var description: String = "",
                            var place: String = "",
                            var subject: String = "",
                            var studentId: String = "",
                            var studentName: String = "")
