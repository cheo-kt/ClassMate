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
    suspend fun eliminateRequestBroadcast(requestId: String, subjectID: String)
}


class RequestBroadcastRepositoryImpl(
    val requestBroadcastServices : RequestBroadcastService = RequestBroadcastServicesImpl()
): RequestBroadcastRepository {


    override suspend fun createRequestBroadcast( requestBroadcast: RequestBroadcast) {

        val requestId = UUID.randomUUID().toString()
        val requestWithId = requestBroadcast.copy(id = requestId)

         requestBroadcastServices.checkForOverlappingRequest(
            Firebase.auth.currentUser?.uid ?: "",
            requestBroadcast)
        requestBroadcastServices.createRequestInMainCollection(requestWithId)
        requestBroadcastServices.createRequestForStudent(Firebase.auth.currentUser?.uid ?: "", requestWithId)
        requestBroadcastServices.createRequestForSubject(requestBroadcast.subjectID, requestId)

    }

    override suspend fun eliminateRequestBroadcast(requestId: String, subjectID: String) {
        val currentUserID = Firebase.auth.currentUser?.uid ?: return

        requestBroadcastServices.deleteRequestFromMainCollection(requestId)

        requestBroadcastServices.deleteRequestForStudent(currentUserID, requestId)

        requestBroadcastServices.deleteRequestForSubject(subjectID, requestId)
    }



}

