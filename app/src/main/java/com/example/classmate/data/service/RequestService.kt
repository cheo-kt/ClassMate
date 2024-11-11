package com.example.classmate.data.service

import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestService {

    suspend fun  createRequest(request: Request)
}



class RequestServicesImpl: RequestService {

    override suspend fun createRequest(request: Request) {
        Firebase.firestore
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }

}