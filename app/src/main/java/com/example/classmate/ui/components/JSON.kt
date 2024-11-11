package com.example.classmate.ui.components

import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.RequestBroadcast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

    fun serializeList(list: List<Any>): String {
        val gson = Gson()  // Usa Gson o cualquier librería de tu preferencia
        return gson.toJson(list)
    }

fun deserializeListRequestBroadcast(json: String?): List<RequestBroadcast> {
    val gson = Gson()
    val listType = object : TypeToken<List<RequestBroadcast>>() {}.type
    return gson.fromJson(json, listType)
}

fun deserializeListAppointment(json: String?): List<Appointment> {
    val gson = Gson()
    val listType = object : TypeToken<List<Appointment>>() {}.type
    return gson.fromJson(json, listType)
}

