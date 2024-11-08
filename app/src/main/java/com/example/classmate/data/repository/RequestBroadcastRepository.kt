package com.example.classmate.data.repository

import com.example.classmate.data.service.RequestBroadcastService
import com.example.classmate.data.service.RequestBroadcastServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.firestore.FirebaseFirestore

interface RequestBroadcastRepository {
    suspend fun  createRequestBroadcast(requestBroadcast: RequestBroadcast)
}


class RequestBroadcastRepositoryImpl(
    val requestBroadcastServices : RequestBroadcastService = RequestBroadcastServicesImpl()
): RequestBroadcastRepository {


    override suspend fun createRequestBroadcast(requestBroadcast: RequestBroadcast) {
        val documentId = FirebaseFirestore.getInstance().collection("requestBroadcast").document().id
        requestBroadcast.id = documentId
        requestBroadcastServices.createRequestBroadcast(requestBroadcast)
    }


}

