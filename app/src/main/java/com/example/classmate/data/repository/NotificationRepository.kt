package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.domain.model.Notification
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

interface NotificationRepository {
    suspend fun createNotification(notification: Notification)
    suspend fun createNotificationForMonitor(notification: Notification)
    suspend fun loadMoreNotifications(limit: Int, notification: Notification?): List<Notification?>
    suspend fun loadMoreNotificationsForMonitor(limit: Int, notification: Notification?): List<Notification?>
    suspend fun deleteNotification(notification: Notification)
    suspend fun deleteRecordatory()
    suspend fun deleteNotificationById(idNotification: String)
}

class NotificationRepositoryImpl(
    val notificationServices : NotificationService = NotificationServiceImpl()
): NotificationRepository {

    override suspend fun createNotification(notification: Notification){
        val documentId = FirebaseFirestore.getInstance().collection("notification").document().id
        notification.id = documentId
        notificationServices.createNotification(notification)
    }
    override suspend fun createNotificationForMonitor(notification: Notification){
        notificationServices.createNotificationForMonitor(notification)
    }

    override suspend fun loadMoreNotifications(
        limit: Int,
        notification: Notification?
    ): List<Notification?> {
        val user=Firebase.auth.currentUser!!.uid
        return notificationServices.loadMoreNotifications(limit, notification,user)
    }

    override suspend fun loadMoreNotificationsForMonitor(
        limit: Int,
        notification: Notification?
    ): List<Notification?> {
        val user=Firebase.auth.currentUser!!.uid
        return notificationServices.loadMoreNotificationsForMonitor(limit, notification,user)
    }

    override suspend fun deleteNotification(notification: Notification) {
        val userid = Firebase.auth.currentUser!!.uid
        notificationServices.deleteNotification(notification,userid)
    }
    override suspend fun deleteNotificationById(idNotification: String) {
        val userid = Firebase.auth.currentUser!!.uid
        notificationServices.deleteNotificationById(idNotification,userid)
    }
    override suspend fun deleteRecordatory(){
        val userid = Firebase.auth.currentUser!!.uid
        notificationServices.deleteRecordatory(userid)
    }
}