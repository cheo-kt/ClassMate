package com.example.classmate.data.repository

import android.net.Uri
import com.example.classmate.domain.model.Message
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface AppointmentChatRepository {
    suspend fun sendMessage(appointmentId: String, message: Message, uri: Uri?)
    suspend fun getMessages(appointmentId: String): List<Message?>
    suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit)
}
class AppointmentChatRepositoryImpl : AppointmentChatRepository {

    override suspend fun sendMessage(appointmentId: String, message: Message, uri: Uri?) {
        uri?.let {
            val imageID = UUID.randomUUID().toString()
            message.imageID = imageID
            uploadImage(it, imageID)
        }
        message.authorID = Firebase.auth.currentUser?.uid ?: ""
        Firebase.firestore.collection("appointments")
            .document(appointmentId)
            .collection("messages")
            .document(message.id)
            .set(message)
            .await()
    }

    override suspend fun getMessages(appointmentId: String): List<Message?> {
        val result = Firebase.firestore.collection("appointments")
            .document(appointmentId)
            .collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .await()
        return result.documents.map { it.toObject(Message::class.java) }
    }

    override suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit) {
        Firebase.firestore.collection("appointments")
            .document(appointmentId)
            .collection("messages")
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        val message = change.document.toObject(Message::class.java)
                        message.isMine = message.authorID == Firebase.auth.currentUser?.uid
                        CoroutineScope(Dispatchers.IO).launch { callback(message) }
                    }
                }
            }
    }

    private suspend fun uploadImage(uri: Uri, imageID: String) {
        Firebase.storage.reference.child("appointmentChatImages/$imageID").putFile(uri).await()
    }

    suspend fun getImageURL(imageID: String): String {
        return Firebase.storage.reference.child("appointmentChatImages/$imageID").downloadUrl.await().toString()
    }
}
