package com.example.classmate.domain.model

data class Subject(
    val nombre: String  ,
    var precio: String // Puedes cambiar a otro tipo de datos si el precio es numérico


){
    // Constructor vacío requerido para Firebase
    constructor() : this("", "")

}
