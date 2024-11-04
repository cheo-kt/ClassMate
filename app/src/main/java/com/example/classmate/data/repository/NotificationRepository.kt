package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.domain.model.Notification
import com.google.firebase.firestore.FirebaseFirestore

interface NotificationRepository {
    suspend fun  createNotification(notification: Notification)
}

class NotificationRepositoryImpl(
    val notificationServices : NotificationService = NotificationServiceImpl()
): NotificationRepository {

    override suspend fun createNotification(notification: Notification){
        val documentId = FirebaseFirestore.getInstance().collection("notification").document().id
        notification.id = documentId
        notificationServices.createNotification(notification)

    }


}