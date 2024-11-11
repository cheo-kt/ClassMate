package com.example.classmate.ui.components

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

    fun serializeList(list: List<Any>): String {
        val gson = Gson()  // Usa Gson o cualquier librería de tu preferencia
        return gson.toJson(list)
    }

fun deserializeList(json: String?): List<Any> {
    val gson = Gson()
    val listType = object : TypeToken<List<Any>>() {}.type
    return gson.fromJson(json, listType)
}

