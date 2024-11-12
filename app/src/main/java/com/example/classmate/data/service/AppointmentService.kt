package com.example.classmate.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.classmate.domain.model.Monitor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

interface AppointmentService {
    suspend fun deleteAppointmentFromMainCollection(appointmentId: String)
    suspend fun deleteAppointmentForStudent(studentId: String, appointmentId: String)
    suspend fun deleteAppointmentForMonitor(monitorId: String, appointmentId: String)
}
class AppointmentServiceImpl: AppointmentService {
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

}
