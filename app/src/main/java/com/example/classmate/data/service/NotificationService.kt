package com.example.classmate.data.service

import android.util.Log
import com.example.classmate.domain.model.Notification
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface NotificationService {

    suspend fun createNotification(notification: Notification)
    suspend fun createNotificationForMonitor(notification: Notification)
    suspend fun loadMoreNotifications(limit: Int, notification: Notification?, studentId:String): List<Notification?>
    suspend fun loadMoreNotificationsForMonitor(limit: Int, notification: Notification?, monitorId:String): List<Notification?>
    suspend fun createNotificationQualification(notification: Notification)
    suspend fun deleteNotification(notification: Notification, userId: String)
    suspend fun deleteRecordatory(userId: String)
    suspend fun deleteNotificationById(idNotification: String, userId: String)
}

class NotificationServiceImpl: NotificationService {
    override suspend fun createNotification(notification: Notification) {
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

    override suspend fun createNotificationForMonitor(notification: Notification) {
        Firebase.firestore
            .collection("notification")
            .document(notification.id)
            .set(notification)
            .await()
        Firebase.firestore
            .collection("Monitor")
            .document(notification.monitorId)
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

    override suspend fun loadMoreNotifications(limit: Int, notification: Notification?, studentId:String): List<Notification?> {
       val querySnapshot = Firebase.firestore.collection("student")
                .document(studentId)
                .collection("notification")
                .orderBy("id")
                .startAfter(notification?.id)
                .limit(limit.toLong())
                .get()
                .await()
        return  querySnapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
    }

    override suspend fun loadMoreNotificationsForMonitor(limit: Int, notification: Notification?, monitorId:String): List<Notification?> {
        val querySnapshot = Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("notification")
            .orderBy("id")
            .startAfter(notification?.id)
            .limit(limit.toLong())
            .get()
            .await()
        Log.e(">>>S",querySnapshot.documents.size.toString())
        return  querySnapshot.documents.mapNotNull { it.toObject(Notification::class.java) }
    }

    override suspend fun deleteRecordatory(userId: String) {
        val now = Timestamp.now()

        val result = Firebase.firestore.collection("student")
            .document(userId)
            .collection("notification")
            .whereIn("type", listOf("RECORDATORIO", "ACEPTACION","RECHAZO")) //Traeme las notis de tipo RECORDATORIO, RECHAZO o ACEPTACION
            .whereLessThan("dateMonitoring", now) //Seleccioname aquellas notis que sean menor a la fecha actual para borrar
            .get()
            .await()
        result.forEach { document ->
            val notification = document.toObject(Notification::class.java)
            deleteNotification(notification, userId)
        }
    }

    override suspend fun deleteNotificationById(idNotification: String, userId: String) {
        Firebase.firestore
            .collection("notification")
            .document(idNotification)
            .delete()
            .await()

        Firebase.firestore
            .collection("Monitor")
            .document(userId)
            .collection("notification")
            .document(idNotification)
            .delete()
            .await()
    }

    override suspend fun deleteNotification(notification: Notification, userId: String) {
        //General
        Firebase.firestore
            .collection("notification")
            .document(notification.id)
            .delete()
            .await()
        //Student
        Firebase.firestore
            .collection("student")
            .document(userId)
            .collection("notification")
            .document(notification.id)
            .delete()
            .await()
    }

}
