package com.example.classmate.data.service

import com.example.classmate.domain.model.Notification
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface NotificationService {

    suspend fun createNotification(notification: Notification)
}

class NotificationServiceImpl: NotificationService {
    override suspend fun createNotification(notification: Notification) {
        Firebase.firestore
            .collection("notification")
            .document(notification.id)
            .set(notification)
            .await()

    }

}
