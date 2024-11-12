package com.example.classmate.data.service

import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestService {

    suspend fun  createRequest(request: Request)
    suspend fun createRequestForStudent(studentID:String,request:Request)
    suspend fun  createRequestForMonitor(monitorID:String,request:Request)
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


}