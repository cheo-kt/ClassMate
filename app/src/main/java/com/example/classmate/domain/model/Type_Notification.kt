package com.example.classmate.domain.model

enum class Type_Notification(val description: String) {
    RECHAZO("RechazoS"),
    ACEPTACION("AceptaciónS"),
    CALIFICACION("CalificacionS"),
    RECORDATORIO("RecordatorioSM"),
    INTERES("InteresM"),
    SOLICITUD("SolicitudM"),
    DEFAULT("Default")
}