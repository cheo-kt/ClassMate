package com.example.classmate.domain.model

class Monitor(
    var id: String = "",
    var name: String = "",
    var lastname: String = "",
    var phone: String = "",
    var subjects: List<Subject> = ArrayList<Subject>(),
    var email: String = "",
    var description: String ="",
    var photoUrl: String = "",
    var rating: Int = 0
)