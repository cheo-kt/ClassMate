package com.example.classmate.data.service

import android.util.Log
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Type_Notification
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface RequestBroadcastService {
    suspend fun createRequestInMainCollection(request: RequestBroadcast)
    suspend fun createRequestForStudent(studentId: String, requestWithId: RequestBroadcast)
    suspend fun createRequestForSubject(subjectId:String,requestId: String )
    suspend fun deleteRequestFromMainCollection(requestId: String)
    suspend fun deleteRequestForStudent(studentId: String, requestId: String)
    suspend fun deleteRequestForSubject(subjectId: String, requestId: String)
    suspend fun checkForOverlappingRequest(userId: String, requestBroadcast: RequestBroadcast): RequestBroadcast?
    suspend fun getRandomRequest(monitor: Monitor)
}

class RequestBroadcastServicesImpl: RequestBroadcastService {
    val notificationService: NotificationService = NotificationServiceImpl()

    override suspend fun createRequestInMainCollection(request: RequestBroadcast) {
        Firebase.firestore.collection("requestBroadcast")
            .document(request.id)
            .set(request)
            .await()
    }

    override suspend fun createRequestForStudent(studentId: String, requestWithId: RequestBroadcast) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("requestBroadcast")
            .document(requestWithId.id)
            .set(requestWithId)
            .await()
    }

    override suspend fun createRequestForSubject(subjectId: String, requestId: String) {
        Firebase.firestore.collection("subject")
            .document(subjectId)
            .collection("requestBroadcast")
            .document(requestId)
            .set(mapOf("id" to requestId))
            .await()
    }

    override suspend fun deleteRequestFromMainCollection(requestId: String) {
        Firebase.firestore.collection("requestBroadcast")
            .document(requestId)
            .delete()
            .await()
    }
    override suspend fun deleteRequestForStudent(studentId: String, requestId: String) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("requestBroadcast")
            .document(requestId)
            .delete()
            .await()
    }
    override suspend fun deleteRequestForSubject(subjectId: String, requestId: String) {
        Firebase.firestore.collection("subject")
            .document(subjectId)
            .collection("requestBroadcast")
            .document(requestId)
            .delete()
            .await()
    }

    // Función para verificar superposición de horarios en las subcolecciones del usuario
    override suspend fun checkForOverlappingRequest(userId: String, requestBroadcast: RequestBroadcast): RequestBroadcast? {
        val startTime = requestBroadcast.dateInitial
        val endTime = requestBroadcast.dateFinal
        val subcollections = listOf("requestBroadcast", "appointment", "request")

        for (subcollection in subcollections) {
            val querySnapshot = Firebase.firestore.collection("student")
                .document(userId)
                .collection(subcollection)
                .whereGreaterThan("dateFinal", startTime)
                .whereLessThan("dateInitial", endTime)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no es estudiante")
            }
        }
        return null
    }
    override suspend fun getRandomRequest(monitor: Monitor){
        val subjects = monitor.subjects.map { it.name }
        val db = Firebase.firestore
        val result = db.collection("requestBroadcast")
            .whereIn("subjectname", subjects)
            .whereEqualTo("notificationGenerated", false)
            .get()
            .await()
        val randomRequest = result.documents.randomOrNull()?.toObject(RequestBroadcast::class.java)
        if (randomRequest != null) {
            notificationService.createNotificationForMonitor(
                Notification(
                    UUID.randomUUID().toString(),
                    Timestamp.now(),
                    Timestamp.now(),
                    "¡Te podría interesar!",
                    randomRequest.subjectname,
                    randomRequest.studentId,
                    randomRequest.studentName,
                    monitor.id,
                    monitor.name,
                    Type_Notification.INTERES
                )
            )
            db.collection("requestBroadcast")
                .document(randomRequest.id)
                .update("notificationGenerated  ", true)
                .await()
            }
    }
}