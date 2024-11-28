package com.example.classmate.data.service

import android.util.Log
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.RequestType
import com.example.classmate.domain.model.Subject
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
    suspend fun getRequestBroadcastType():List<RequestType>
    suspend fun getRequestBroadcastByType(type: String,monitor: Monitor): List<RequestBroadcast>
    suspend fun getRequestBroadcastByDateRange(timeStampInitial: Timestamp,timeStampFinal:Timestamp,monitor: Monitor): List<RequestBroadcast>

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
                .whereGreaterThanOrEqualTo("dateFinal", startTime)
                .whereLessThanOrEqualTo("dateInitial", endTime)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "ya existe")
            }
        }
        return null
    }
    override suspend fun getRandomRequest(monitor: Monitor){
        val subjects = monitor.subjects.map { it.name }
        if (subjects.isNotEmpty()) {
            val result = Firebase.firestore.collection("requestBroadcast")
                .whereIn("subjectname", subjects)
                .whereEqualTo("notificationGenerated", false)
                .get()
                .await()
            val randomRequest =
                result.documents.randomOrNull()?.toObject(RequestBroadcast::class.java)
            val numberOfInterestRequest =
                notificationService.getNumberOfInteresNotifications(monitor.id)
            if (randomRequest != null && numberOfInterestRequest == 0) {
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
                Firebase.firestore.collection("requestBroadcast")
                    .document(randomRequest.id)
                    .update("notificationGenerated", true)
                    .await()
            }
        }
    }

    override suspend fun getRequestBroadcastType(): List<RequestType> {
        return try{
            Firebase.firestore
                .collection("requestType")
                .get()
                .await()
                .toObjects(RequestType::class.java)
        }catch (e: Exception){
            emptyList<RequestType>()
        }
    }
    override suspend fun getRequestBroadcastByType(type: String,monitor: Monitor): List<RequestBroadcast> {
        val subjects = monitor.subjects.map { it.name }
        return  try{
            Firebase.firestore
                .collection("requestBroadcast")
                .whereIn("subjectname", subjects)
                .whereEqualTo("type",type)
                .get()
                .await()
                .toObjects(RequestBroadcast::class.java)
        }catch (e: Exception){
            emptyList<RequestBroadcast>()
        }
    }

    override suspend fun getRequestBroadcastByDateRange(timeStampInitial: Timestamp,timeStampFinal:Timestamp,monitor: Monitor): List<RequestBroadcast> {
        val subjects = monitor.subjects.map { it.name }
        return try {
            Firebase.firestore
                .collection("requestBroadcast")
                .whereIn("subjectname", subjects)
                .whereGreaterThan("dateInitial",timeStampInitial)
                .whereLessThan("dateInitial",timeStampFinal)
                .get()
                .await()
                .toObjects(RequestBroadcast::class.java)
        }catch (e:Exception){
            emptyList<RequestBroadcast>()
        }
    }


}