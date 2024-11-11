package com.example.classmate.domain.model

data class Subject(
    val id: String = "",
    val name: String = "",
    val monitorsID: List<String> = emptyList(),
    val requestBroadcast: List<RequestBroadcast> = emptyList()
){
    constructor() : this("", "", emptyList(), emptyList())

}
