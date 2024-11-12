package com.example.classmate.data.service

import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface NotificationService {

    suspend fun createNotification(notification: Notification)
    suspend fun loadMoreNotifications(limit: Int, notification: Notification?, userId:String): List<Notification?>
    suspend fun createNotificationQualification(notification: Notification)
}

class NotificationServiceImpl: NotificationService {
    override suspend fun createNotification(notification: Notification) {
        Firebase.firestore
            .collection("notification")
            .document(notification.id)
            .set(notification)
            .await()
    }

    override suspend fun createNotificationQualification(notification: Notification) {
        Firebase.firestore
            .collection("notification")
            .document(notification.id)
            .set(notification)
            .await()
        Firebase.firestore
            .collection("student")
            .document(notification.studentId)
            .collection("notification")
            .document(notification.id)
            .set(notification)
            .await()
    }

    override suspend fun loadMoreNotifications(limit: Int, notification: Notification?, userId:String): List<Notification?> {
        return try {
            val querySnapshot = Firebase.firestore.collection("student")
                .document(userId)
                .collection("notification")
                .orderBy("id")
                .startAfter(notification?.id)
                .limit(limit.toLong())
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
