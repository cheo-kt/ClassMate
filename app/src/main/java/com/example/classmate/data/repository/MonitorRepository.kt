package com.example.classmate.data.repository

import android.net.Uri
import com.example.classmate.data.service.MonitorServices
import com.example.classmate.data.service.MonitorServicesImpl
import com.example.classmate.domain.model.Monitor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Context
import android.util.Log
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Subject
import java.util.UUID

interface MonitorRepository {



    suspend fun  createMonitor(monitor: Monitor)
    suspend fun  getCurrentMonitor(): Monitor?
    suspend fun updateMonitorPhoto(id: String, imageUri: Uri, context: Context): String
    suspend fun updateMonitorInformation(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url:String)
    suspend fun searchMonitor(name:String):List<Monitor?>
    suspend fun getMonitors(limit: Int, monitor: Monitor?):List<Monitor?>
    suspend fun getBroadRequest(limit: Int, request: RequestBroadcast?):List<RequestBroadcast?>
    suspend fun calificateMonitor(opinionsAndQualifications:OpinionsAndQualifications, monitorId:String)
    suspend fun getMonitorById(id: String): Monitor? // Nuevo método
    suspend fun loadMoreOpinions(limit: Int, lastOpinion: OpinionsAndQualifications?, monitorId: String):List<OpinionsAndQualifications>
    suspend fun getAppointments():List<Appointment?>
    suspend fun getMonitorImage(imageURL:String):String
    suspend fun getRequest(limit: Int, request: Request?):List<Request?>
    suspend fun getAppointmentsUpdate(): List<Pair<Appointment, Boolean>>
    suspend fun searchMonitorBySubject(subjectIds: List<String>): List<Monitor?>
    suspend fun searchSubjectsByName(subjectName: String):List<RequestBroadcast?>
    suspend fun searchSubjectsByNameRequest(subjectName: String): List<Request?>



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
    override suspend fun searchMonitor(name:String):List<Monitor?>{
        return monitorServices.searchMonitorByName(name)
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

    override suspend fun loadMoreOpinions(
        limit: Int,
        lastOpinion: OpinionsAndQualifications?,
        monitorId: String
    ):List<OpinionsAndQualifications> {
        Log.e(">>>>", "Estoy en repo")
        return monitorServices.loadMoreOpinions(limit, lastOpinion, monitorId)

    }
    override suspend fun getAppointments(): List<Appointment?> {
        return  monitorServices.getAppointments(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun getMonitorImage(imageURL: String): String {
        return monitorServices.getImageDownloadUrl(imageURL)
    }

    override suspend fun getRequest(limit: Int, request: Request?): List<Request?> {
        return monitorServices.getRequest(limit, request)

    }

    override suspend fun getAppointmentsUpdate(): List<Pair<Appointment, Boolean>>{
        return  monitorServices.getAppointmentsUpdate(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun searchMonitorBySubject(subjectIds: List<String>): List<Monitor?> {
        return monitorServices.searchMonitorBySubject(subjectIds)
    }

    override suspend fun searchSubjectsByName(subjectName: String): List<RequestBroadcast?> {
        return monitorServices.searchSubjectsByName(subjectName)
    }

    override suspend fun searchSubjectsByNameRequest(subjectName: String): List<Request?> {
        return monitorServices.searchSubjectsByNameRequest(subjectName)
    }

}