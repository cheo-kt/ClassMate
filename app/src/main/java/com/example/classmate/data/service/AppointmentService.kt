package com.example.classmate.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

interface AppointmentService {
    suspend fun createAppointmentInGeneral(appointment: Appointment)
    suspend fun createAppointmentForStudent(appointment: Appointment)
    suspend fun createAppointmentForMonitor(appointment: Appointment)
    suspend fun verifyAppointmentForStudent(date: Timestamp, userId:String)
    suspend fun deleteAppointmentFromMainCollection(appointmentId: String)
    suspend fun deleteAppointmentForStudent(studentId: String, appointmentId: String)
    suspend fun deleteAppointmentForMonitor(monitorId: String, appointmentId: String)
}
class AppointmentServiceImpl: AppointmentService {
    val notificationService : NotificationService = NotificationServiceImpl()
    override suspend fun createAppointmentInGeneral(appointment: Appointment) {
        Firebase.firestore.collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
    }
    override suspend fun deleteAppointmentFromMainCollection(appointmentId: String) {
        Firebase.firestore.collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun deleteAppointmentForStudent(studentId: String, appointmentId: String) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun deleteAppointmentForMonitor(monitorId: String, appointmentId: String) {
        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun createAppointmentForStudent(appointment: Appointment) {
        Firebase.firestore.collection("student")
            .document(appointment.studentId)
            .collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
    }

    override suspend fun createAppointmentForMonitor(appointment: Appointment) {
        Firebase.firestore.collection("Monitor")
            .document(appointment.monitorId)
            .collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
    }
    override suspend fun verifyAppointmentForStudent(date: Timestamp, userId:String) {
        val result = Firebase.firestore.collection("student")
            .document(userId)
            .collection("appointment")
            .whereLessThan("dateFinal", date)
            .get()
            .await()
        val appointmentFinished = result.documents.mapNotNull { it.toObject(Appointment::class.java) }
        appointmentFinished.forEach{ appointment ->
            notificationService.createNotificationQualification(Notification(
                UUID.randomUUID().toString(),appointment.dateFinal,
                "¿Que tal tu monitoria?",appointment.subjectname,
                appointment.studentId,appointment.studentName, appointment.monitorId,
                appointment.monitorName
            ))
            deleteAppointment(appointment)
        }
    }
    private suspend fun deleteAppointment(appointment: Appointment){
        //Monitor
        Firebase.firestore.collection("Monitor")
            .document(appointment.monitorId)
            .collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
        //Student
        Firebase.firestore.collection("student")
            .document(appointment.studentId)
            .collection("appointment")
            .document(appointment.id)
            .delete()
            .await()
        //General
        Firebase.firestore.collection("appointment")
            .document(appointment.id)
            .delete()
            .await()
    }
}
