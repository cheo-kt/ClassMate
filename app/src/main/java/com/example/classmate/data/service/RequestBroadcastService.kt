package com.example.classmate.data.service

import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestBroadcastService {
    suspend fun  createRequestBroadcast(requestBroadcast: RequestBroadcast)
}

class RequestBroadcastServicesImpl: RequestBroadcastService {

    override suspend fun createRequestBroadcast(requestBroadcast: RequestBroadcast) {
        Firebase.firestore
            .collection("requestBroadcast")
            .document(requestBroadcast.id)
            .set(requestBroadcast)
            .await()
    }

}