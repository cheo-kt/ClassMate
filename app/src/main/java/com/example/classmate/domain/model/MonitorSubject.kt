package com.example.classmate.domain.model

data class MonitorSubject(
    val subjectId: String,
    var name: String,
    var price: String
)
{
    constructor() : this("", "","")
}