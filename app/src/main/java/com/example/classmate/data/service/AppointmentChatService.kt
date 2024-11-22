package com.example.classmate.data.service

import android.net.Uri
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Message
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface AppointmentChatService {
    suspend fun getAppointmentById(appointmentId: String): Appointment
    suspend fun sendMessage(message: Message, appointmentId: String)
    suspend fun getMessages(appointmentId: String): List<Message?>
    fun getLiveMessages(appointmentId: String, callback: (QueryDocumentSnapshot) -> Unit)
    suspend fun uploadImage(uri: Uri, id: String)
    suspend fun getImageURLByID(id: String): String
}

class AppointmentChatServiceImpl : AppointmentChatService {
    override suspend fun getAppointmentById(appointmentId: String): Appointment {
        val document = Firebase.firestore.collection("appointment")
            .document(appointmentId)
            .get()
            .await()
        return document.toObject(Appointment::class.java) ?: Appointment()
    }

    override suspend fun sendMessage(message: Message, appointmentId: String) {
        Firebase.firestore.collection("appointment")
            .document(appointmentId)
            .collection("messages")
            .document(message.id)
            .set(message)
            .await()
    }
    override suspend fun getMessages(appointmentId: String): List<Message?> {
        val result = Firebase.firestore
            .collection("appointment")
            .document(appointmentId)
            .collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .await()
        return result.documents.map { it.toObject(Message::class.java) }
    }
    override fun getLiveMessages(appointmentId: String, callback: (QueryDocumentSnapshot) -> Unit) {
        Firebase.firestore.collection("appointment")
            .document(appointmentId)
            .collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        callback(change.document)
                    }
                }
            }
    }


    override suspend fun uploadImage(uri: Uri, id: String) {
        Firebase.storage.reference.child("images").child("chatImages").child(id).putFile(uri).await()
    }

    override suspend fun getImageURLByID(id: String): String {
        return Firebase.storage.reference.child("images").child("chatImages").child(id).downloadUrl.await().toString()
    }


}