package com.example.classmate.data.repository

import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.data.service.RequestService
import com.example.classmate.data.service.RequestServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

interface RequestRepository {
    suspend fun  createRequest(studentID:String, monitorID:String,request: Request)
    suspend fun  deleteRequest(studentID:String, monitorID:String,requestId: String)

}




class RequestRRepositoryImpl(
    val requestServices : RequestService = RequestServicesImpl()
): RequestRepository {


    override suspend fun createRequest(studentID:String, monitorID:String,request:Request) {
        val documentId = UUID.randomUUID().toString()
        request.id = documentId

        requestServices.checkForOverlappingRequest(
            Firebase.auth.currentUser?.uid ?: "",
            request)

        requestServices.createRequest(request)
        requestServices.createRequestForStudent(studentID,request)
        requestServices.createRequestForMonitor(monitorID,request)
    }

    override suspend fun deleteRequest(studentID: String, monitorID: String, requestId: String) {
        requestServices.deleteRequestFromMainCollection(requestId)
        requestServices.deleteRequestForMonitor(monitorID,requestId)
        requestServices.deleteRequestForStudent(studentID,requestId)
    }


}