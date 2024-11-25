package com.example.classmate.data.repository

import com.example.classmate.data.service.RequestBroadcastService
import com.example.classmate.data.service.RequestBroadcastServicesImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

interface RequestBroadcastRepository {
    suspend fun  createRequestBroadcast(requestBroadcast: RequestBroadcast)
    suspend fun eliminateRequestBroadcast(requestId: String, subjectID: String, studentId: String)
    suspend fun loadRandomReqBroadcast(monitor: Monitor)
}


class RequestBroadcastRepositoryImpl(
    val requestBroadcastServices : RequestBroadcastService = RequestBroadcastServicesImpl()
): RequestBroadcastRepository {


    override suspend fun createRequestBroadcast( requestBroadcast: RequestBroadcast) {

        val requestId = UUID.randomUUID().toString()
        val requestWithId = requestBroadcast.copy(id = requestId)
        try {
            requestBroadcastServices.checkForOverlappingRequest(
                Firebase.auth.currentUser?.uid ?: "",
                requestBroadcast)



            requestBroadcastServices.createRequestInMainCollection(requestWithId)
            requestBroadcastServices.createRequestForStudent(Firebase.auth.currentUser?.uid ?: "", requestWithId)
            requestBroadcastServices.createRequestForSubject(requestBroadcast.subjectID, requestId)
        }catch (e: FirebaseAuthException){
            throw e
        }


    }

    override suspend fun eliminateRequestBroadcast(requestId: String, subjectID: String, studentId: String) {

        requestBroadcastServices.deleteRequestFromMainCollection(requestId)

        requestBroadcastServices.deleteRequestForStudent(studentId, requestId)

        requestBroadcastServices.deleteRequestForSubject(subjectID, requestId)
    }

    override suspend fun loadRandomReqBroadcast(monitor: Monitor) {
        requestBroadcastServices.getRandomRequest(monitor)
    }

}

