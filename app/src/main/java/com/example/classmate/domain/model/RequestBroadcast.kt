package com.example.classmate.domain.model

import com.google.firebase.Timestamp

data class RequestBroadcast(var id: String = "",
                            var mode_class: String = "",
                            var type: String = "",
                            var dateInitial: Timestamp = Timestamp.now(),
                            var dateFinal: Timestamp = Timestamp.now(),
                            var description: String = "",
                            var place: String = "",
                            var subjectID: String = "",
                            var subjectname: String,
                            var studentId: String = "",
                            var studentName: String = "")
