package com.example.classmate.data.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.classmate.domain.model.Monitor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

interface MonitorServices {

    suspend fun createMonitor(monitor: Monitor)
    suspend fun  getMonitorById(id:String): Monitor?
    suspend fun uploadProfileImage(id: String, uri: Uri, context: Context): String
    suspend fun updateMonitorField(id: String, field: String, value: Any)
    suspend fun updateMonitorImageUrl(id:String,url: String)
    suspend fun getMonitors():List<Monitor?>
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
    override suspend fun uploadProfileImage(id: String, uri: Uri, context: Context, ): String  {
        val storageRef = Firebase.storage.reference.child("images/monitors/$id.jpg")
        val bitmap= withContext(Dispatchers.IO){
            val inputStream = context.contentResolver.openInputStream(uri)

            BitmapFactory.decodeStream(inputStream)
        }
        val compressedBitmapStream= ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, compressedBitmapStream)

        val compressedData = compressedBitmapStream.toByteArray()

        storageRef.putBytes(compressedData).await()

        return storageRef.downloadUrl.await().toString()
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

    override suspend fun getMonitors():List<Monitor?> {
    val monitorList = Firebase.firestore
            .collection("Monitor")
            .get()
            .await()
        return monitorList.documents.map { document ->
            document.toObject(Monitor::class.java)
        }
    }
}