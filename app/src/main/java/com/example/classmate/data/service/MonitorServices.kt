package com.example.classmate.data.service

import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface MonitorServices {

    suspend fun createMonitor(monitor: Monitor)
    suspend fun  getMonitorById(id:String): Monitor?
}

class MonitorServicesImpl: MonitorServices {
    override suspend fun createMonitor(monitor: Monitor) {
        Firebase.firestore
            .collection("monitor")
            .document(monitor.id)
            .set(monitor)
            .await()
    }

    override suspend fun getMonitorById(id: String): Monitor? {
        val user = Firebase.firestore
            .collection("student")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(Monitor::class.java)
        return userObject
    }
}