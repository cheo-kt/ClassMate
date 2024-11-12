package com.example.classmate.data.repository

import android.net.Uri
import com.example.classmate.data.service.MonitorServices
import com.example.classmate.data.service.MonitorServicesImpl
import com.example.classmate.domain.model.Monitor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Context
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.domain.model.RequestBroadcast
import java.util.UUID

interface MonitorRepository {



    suspend fun  createMonitor(monitor: Monitor)
    suspend fun  getCurrentMonitor(): Monitor?
    suspend fun updateMonitorPhoto(id: String, imageUri: Uri, context: Context): String
    suspend fun updateMonitorInformation(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url:String)
    suspend fun getMonitors(limit: Int, monitor: Monitor?):List<Monitor?>
    suspend fun getBroadRequest(limit: Int, request: RequestBroadcast?):List<RequestBroadcast?>
    suspend fun calificateMonitor(opinionsAndQualifications:OpinionsAndQualifications, monitorId:String)
    suspend fun getMonitorById(id: String): Monitor? // Nuevo método

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

    override suspend fun updateMonitorPhoto(id: String, imageUri: Uri, context: Context): String {
        return monitorServices.uploadProfileImage(id, imageUri, context)
    }

    override suspend fun updateMonitorInformation(id: String, field: String, value: Any) {
        monitorServices.updateMonitorField(id,field, value)
    }

    override suspend fun updateMonitorImageUrl(id:String,url: String) {
        monitorServices.updateMonitorImageUrl(id,url)
    }

    override suspend fun getMonitors(limit: Int, monitor: Monitor?):List<Monitor?> {
        return monitorServices.getMonitors(limit,monitor)
    }
    override suspend fun getBroadRequest(limit: Int, request: RequestBroadcast?):List<RequestBroadcast?> {
        return monitorServices.getBroadRequest(limit, request)
    }

    override suspend fun calificateMonitor(
        opinionsAndQualifications: OpinionsAndQualifications,
        monitorId: String
    ) {
        val requestId = UUID.randomUUID().toString()
        opinionsAndQualifications.id = requestId
       return monitorServices.calificateMonitor(opinionsAndQualifications,monitorId)
    }

    override suspend fun getMonitorById(id: String): Monitor? {
        return monitorServices.getMonitorById(id)
    }

}