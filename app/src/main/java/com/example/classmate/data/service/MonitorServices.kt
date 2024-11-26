package com.example.classmate.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.net.Uri
import android.util.Log
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Subject
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.math.round
import kotlin.math.roundToInt

interface MonitorServices {

    suspend fun createMonitor(monitor: Monitor)
    suspend fun  getMonitorById(id:String): Monitor?
    suspend fun uploadProfileImage(id: String, uri: Uri, context: Context,oldImageID:String): String
    suspend fun updateMonitorField(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url: String)
    suspend fun getMonitors(limit: Int, monitor: Monitor?):List<Monitor?>
    suspend fun searchMonitorByName(name:String):List<Monitor?>
    suspend fun getBroadRequest(limit: Int, broadRequest: RequestBroadcast?): List<RequestBroadcast>
    suspend fun calificateMonitor(opinionsAndQualifications: OpinionsAndQualifications,monitorId:String, )
    suspend fun getOpinionMonitor(monitorId:String, limit: Int)
    suspend fun loadMoreOpinions(limit: Int, lastOpinion: OpinionsAndQualifications?, monitorId: String):List<OpinionsAndQualifications>
    suspend fun getAppointments(idStudent:String):List<Appointment?>
    suspend fun getRequest(limit: Int, request: Request?): List<Request>
    suspend fun getImageDownloadUrl(imageUrl:String):String
    suspend fun getAppointmentsUpdate(idStudent: String): List<Pair<Appointment, Boolean>>
    suspend fun searchMonitorBySubject(subjectIds: List<String>):List<Monitor?>
}

class MonitorServicesImpl: MonitorServices {
    override suspend fun createMonitor(monitor: Monitor){
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
    override suspend fun uploadProfileImage(id: String, uri: Uri, context: Context,oldImageID:String): String  {
        if(oldImageID.isNotEmpty() && oldImageID != "noImage"){
            Firebase.storage
                .reference.child("images/monitors/$oldImageID.jpg")
                .delete()
        }
        val uid = UUID.randomUUID()
        val storageRef = Firebase.storage.reference.child("images/monitors/$uid.jpg")
        val bitmap= withContext(Dispatchers.IO){
            val inputStream = context.contentResolver.openInputStream(uri)

            BitmapFactory.decodeStream(inputStream)
        }
        val compressedBitmapStream= ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, compressedBitmapStream)

        val compressedData = compressedBitmapStream.toByteArray()

        storageRef.putBytes(compressedData).await()

        return uid.toString()
    }

    override suspend fun updateMonitorField(id: String, field: String, value: Any) {
        Firebase.firestore
            .collection("Monitor")
            .document(id)
            .update(field, value)
            .await()
    }

    override suspend fun updateMonitorImageUrl(id:String,url: String) {
        Firebase.firestore
            .collection("Monitor")
            .document(id)
            .update("photoUrl", url)
            .await()
    }

    override suspend fun getMonitors(limit: Int, monitor: Monitor?): List<Monitor> {
        return try {
            val querySnapshot = Firebase.firestore.collection("Monitor")
                .orderBy("id")
                .startAfter(monitor?.id)
                .limit(limit.toLong())
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(Monitor::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchMonitorByName(name: String): List<Monitor?> {

        val result = Firebase.firestore
            .collection("Monitor")
            .whereGreaterThanOrEqualTo("name", name)
            .whereLessThanOrEqualTo("name", name + "\uf8ff")
            .get()
            .await()

        return result.documents.map { document ->
            document.toObject(Monitor::class.java)
        }
    }

    override suspend fun getBroadRequest(limit: Int, broadRequest: RequestBroadcast?): List<RequestBroadcast> {
        return try {
            val querySnapshot = Firebase.firestore.collection("requestBroadcast")
                .orderBy("id")
                .startAfter(broadRequest?.id)
                .limit(limit.toLong())
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(RequestBroadcast::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun calificateMonitor(
        opinionsAndQualifications: OpinionsAndQualifications,
        monitorId: String
    ) {
        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("calificationsOpinions")
            .document(opinionsAndQualifications.id)
            .set(opinionsAndQualifications)
            .await()

        val califications = getCalifications(monitorId)

        val averageRating = califications.average()
            .toBigDecimal()
            .setScale(1, java.math.RoundingMode.HALF_UP)
            .toDouble()

        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .update("rating", averageRating)
            .await()

    }

    override suspend fun getOpinionMonitor(monitorId: String, limit: Int) {


    }

    override suspend fun loadMoreOpinions(
        limit: Int,
        lastOpinion: OpinionsAndQualifications?,
        monitorId: String
    ):List<OpinionsAndQualifications> {

            val querySnapshot = Firebase.firestore.collection("Monitor")
                .document(monitorId)
                .collection("calificationsOpinions")
                .orderBy("id")
                .startAfter(lastOpinion?.id)
                .limit(limit.toLong())
                .get()
                .await()
           return querySnapshot.documents.mapNotNull { it.toObject(OpinionsAndQualifications::class.java) }
    }

    private suspend fun getCalifications(monitorId: String): List<Int> {
        val calificationsList = mutableListOf<Int>()
        val querySnapshot = Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("calificationsOpinions")
            .get()
            .await()

        for (document in querySnapshot) {
            val calification = document.getLong("calification")?.toInt()
            if (calification != null) {
                calificationsList.add(calification)
            }
        }
        return calificationsList
    }


    override suspend fun getAppointments(idStudent:String):List<Appointment?> {
        return try {
            val appointmentList = Firebase.firestore
                .collection("Monitor")
                .document(idStudent)
                .collection("appointment")
                .get()
                .await()

            appointmentList.documents.map { document ->
                document.toObject(Appointment::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getImageDownloadUrl(imageUrl: String): String {
        return  Firebase.storage.reference
            .child("images")
            .child("monitors")
            .child("$imageUrl.jpg")
            .downloadUrl
            .await().toString()
    }

    override suspend fun getRequest(limit: Int, request: Request?): List<Request> {
        return try {
            val querySnapshot = Firebase.firestore.collection("request")
                .orderBy("id")
                .startAfter(request?.id)
                .limit(limit.toLong())
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(Request::class.java) }
        } catch (e: Exception) {
            emptyList()
        }

    }

    override suspend fun getAppointmentsUpdate(idMonitor: String): List<Pair<Appointment, Boolean>> {
        return try {
            val now = Timestamp.now()

            val appointmentList = Firebase.firestore
                .collection("Monitor")
                .document(idMonitor)
                .collection("appointment")
                .whereGreaterThanOrEqualTo("dateFinal", now)
                .get()
                .await()

            appointmentList.documents.mapNotNull { document ->
                val appointment = document.toObject(Appointment::class.java)

                if (appointment == null) return@mapNotNull null
                val unreadMessagesExist = hasUnreadMessages(document.id, idMonitor)

                appointment to unreadMessagesExist
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchMonitorBySubject(subjectIds: List<String>): List<Monitor?> {
        val result = Firebase.firestore
            .collection("Monitor")
            .whereIn("id",subjectIds)
            .get()
            .await()
        return result.documents.map { document ->
            document.toObject(Monitor::class.java)
        }
    }

    private suspend fun hasUnreadMessages(appointmentId: String, idMonitor: String): Boolean {
        return try {
            // Verifica si hay mensajes no leídos
            val unreadMessages = Firebase.firestore
                .collection("appointment")
                .document(appointmentId)
                .collection("messages")
                .whereEqualTo("read", false)
                .whereNotEqualTo("authorID", idMonitor)
                .get()
                .await()

            // Si la colección tiene documentos, significa que hay mensajes no leídos
            unreadMessages.isEmpty.not()
        } catch (e: Exception) {
            false
        }
    }
}
