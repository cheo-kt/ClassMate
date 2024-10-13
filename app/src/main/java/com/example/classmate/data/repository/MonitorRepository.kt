package com.example.classmate.data.repository

import com.example.classmate.data.service.MonitorServices
import com.example.classmate.data.service.MonitorServicesImpl
import com.example.classmate.data.service.StudentServices
import com.example.classmate.data.service.StudentServicesImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface MonitorRepository {



    suspend fun  createMonitor(monitor: Monitor)
    suspend fun  getCurrentMonitor(): Monitor?


}
class MonitorRepositoryImpl(
    val monitorServices : MonitorServices = MonitorServicesImpl()
): MonitorRepository {
    override suspend fun createMonitor(monitor: Monitor) {
        monitorServices.createMonitor(monitor)
    }

    override suspend fun getCurrentMonitor(): Monitor? {
        Firebase.auth.currentUser?.let {
            return monitorServices.getMonitorById(it.uid)
        } ?: run {
            return null
        }
    }

}