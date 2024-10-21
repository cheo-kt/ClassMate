package com.example.classmate.data.service

import android.net.Uri
import com.example.classmate.domain.model.Monitor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

interface MonitorServices {

    suspend fun createMonitor(monitor: Monitor)
    suspend fun  getMonitorById(id:String): Monitor?
    suspend fun uploadProfileImage(id: String,uri: Uri): String
    suspend fun updateMonitorField(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url: String)
}

class MonitorServicesImpl: MonitorServices {
    override suspend fun createMonitor(monitor: Monitor) {
        Firebase.firestore
            .collection("Monitor")
            .document(monitor.id)
            .set(monitor)
            .await()
    }

    override suspend fun getMonitorById(id: String): Monitor? {
        val user = Firebase.firestore
            .collection("Monitor")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(Monitor::class.java)
        return userObject
    }
    override suspend fun uploadProfileImage(id: String,uri: Uri): String  {
        val storageRef = Firebase.storage.reference.child("images/monitors/$id.jpg")
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun updateMonitorField(id: String, field: String, value: Any) {
        Firebase.firestore
            .collection("monitor")
            .document(id)
            .update(field, value)
            .await()
    }

    override suspend fun updateMonitorImageUrl(id:String,url: String) {
        Firebase.firestore
            .collection("monitor")
            .document(id)
            .update("photo", url)
            .await()
    }
}