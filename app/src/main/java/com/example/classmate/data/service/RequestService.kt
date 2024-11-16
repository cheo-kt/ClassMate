package com.example.classmate.data.service

import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestService {

    suspend fun  createRequest(request: Request)
    suspend fun createRequestForStudent(studentID:String,request:Request)
    suspend fun  createRequestForMonitor(monitorID:String,request:Request)
    suspend fun checkForOverlappingRequest(userId: String, request: Request): Request?
}



class RequestServicesImpl: RequestService {

    override suspend fun createRequest(request: Request) {
        Firebase.firestore
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }
    override suspend fun createRequestForStudent(studentId: String, request: Request) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }
    

    override suspend fun createRequestForMonitor(monitorID: String, request: Request) {
        Firebase.firestore.collection("Monitor")
            .document(monitorID)
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }

    // Función para verificar superposición de horarios en las subcolecciones del usuario
    override suspend fun checkForOverlappingRequest(userId: String, request: Request): Request? {
        val startTime = request.dateInitial
        val endTime = request.dateFinal
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
                return throw FirebaseAuthException("ERROR_USER_NOT_FOUND", " ")
            }
        }
        return null
    }


}