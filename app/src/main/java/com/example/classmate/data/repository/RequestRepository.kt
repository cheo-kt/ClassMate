package com.example.classmate.data.repository

import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.data.service.RequestService
import com.example.classmate.data.service.RequestServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

interface RequestRepository {
    suspend fun  createRequest(studentID:String, monitorID:String,request: Request)
}




class RequestRRepositoryImpl(
    val requestServices : RequestService = RequestServicesImpl()
): RequestRepository {


    override suspend fun createRequest(studentID:String, monitorID:String,request:Request) {
        val documentId = UUID.randomUUID().toString()
        request.id = documentId


        requestServices.createRequest(request)
        requestServices.createRequestForStudent(studentID,request)
        requestServices.createRequestForMonitor(monitorID,request)
    }


}