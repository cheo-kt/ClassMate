package com.example.classmate.data.repository

import com.example.classmate.data.service.RequestBroadcastService
import com.example.classmate.data.service.RequestBroadcastServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

interface RequestBroadcastRepository {
    suspend fun  createRequestBroadcast(userID:String,requestBroadcast: RequestBroadcast)
}


class RequestBroadcastRepositoryImpl(
    val requestBroadcastServices : RequestBroadcastService = RequestBroadcastServicesImpl()
): RequestBroadcastRepository {


    override suspend fun createRequestBroadcast(userID:String, requestBroadcast: RequestBroadcast) {

        val requestId = UUID.randomUUID().toString()
        val requestWithId = requestBroadcast.copy(id = requestId)


        requestBroadcastServices.createRequestInMainCollection(requestWithId)
        requestBroadcastServices.createRequestForStudent(userID, requestWithId)
    }


}

