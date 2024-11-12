package com.example.classmate.data.service

import com.example.classmate.domain.model.RequestBroadcast
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


}