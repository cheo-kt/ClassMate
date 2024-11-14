package com.example.classmate.data.service

import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestBroadcastService {
    suspend fun createRequestInMainCollection(request: RequestBroadcast)
    suspend fun createRequestForStudent(studentId: String, requestWithId: RequestBroadcast)
    suspend fun createRequestForSubject(subjectId:String,requestId: String )
    suspend fun deleteRequestFromMainCollection(requestId: String)
    suspend fun deleteRequestForStudent(studentId: String, requestId: String)
    suspend fun deleteRequestForSubject(subjectId: String, requestId: String)
    suspend fun checkForOverlappingRequest(userId: String, requestBroadcast: RequestBroadcast): RequestBroadcast?
}

class RequestBroadcastServicesImpl: RequestBroadcastService {


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

        // Verificar en las subcolecciones del usuario: requestBroadcast, appointment, request
        val subcollections = listOf("requestBroadcast", "appointment", "request")

        for (subcollection in subcollections) {
            val querySnapshot = Firebase.firestore.collection("student") // Aquí asumimos que estás buscando en la colección de estudiante
                .document(userId)
                .collection(subcollection)
                .get()
                .await()
            if (querySnapshot.isEmpty) {
                continue
            }

            // Recorrer los documentos de la subcolección
            for (document in querySnapshot.documents) {
                val existingRequest = document.toObject(RequestBroadcast::class.java)
                if (existingRequest != null) {
                    val existingStartTime = existingRequest.dateInitial
                    val existingEndTime = existingRequest.dateFinal

                    if (isOverlapping(startTime, endTime, existingStartTime, existingEndTime)) {
                        return existingRequest
                    }
                }
            }
        }

        return null // No hay superposición
    }

    private fun isOverlapping(start1: Timestamp, end1: Timestamp, start2: Timestamp, end2: Timestamp): Boolean {
        println(end1)
        println(start2)
        println(end2)
        println(start1)
        return !(end1.seconds <= start2.seconds || end2.seconds <= start1.seconds)
    }


}