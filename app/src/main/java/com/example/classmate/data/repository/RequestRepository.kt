package com.example.classmate.data.repository

import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.data.service.RequestService
import com.example.classmate.data.service.RequestServicesImpl
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.FirebaseFirestore

interface RequestRepository {
    suspend fun  createRequest(request: Request)
}




class RequestRRepositoryImpl(
    val requestServices : RequestService = RequestServicesImpl()
): RequestRepository {


    override suspend fun createRequest(request: Request) {
        val documentId = FirebaseFirestore.getInstance().collection("request").document().id
        request.id = documentId
        requestServices.createRequest(request)
    }


}