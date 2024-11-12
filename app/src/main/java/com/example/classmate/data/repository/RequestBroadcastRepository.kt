package com.example.classmate.data.repository

import com.example.classmate.data.service.RequestBroadcastService
import com.example.classmate.data.service.RequestBroadcastServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

interface RequestBroadcastRepository {
    suspend fun  createRequestBroadcast(requestBroadcast: RequestBroadcast)
}


class RequestBroadcastRepositoryImpl(
    val requestBroadcastServices : RequestBroadcastService = RequestBroadcastServicesImpl()
): RequestBroadcastRepository {


    override suspend fun createRequestBroadcast( requestBroadcast: RequestBroadcast) {

        val requestId = UUID.randomUUID().toString()
        val requestWithId = requestBroadcast.copy(id = requestId)


        requestBroadcastServices.createRequestInMainCollection(requestWithId)
        requestBroadcastServices.createRequestForStudent(Firebase.auth.currentUser?.uid ?: "", requestWithId)
        requestBroadcastServices.createRequestForSubject(requestBroadcast.subjectID, requestId)
    }


}

