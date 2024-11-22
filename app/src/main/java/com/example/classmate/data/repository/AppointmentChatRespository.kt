package com.example.classmate.data.repository

import android.net.Uri
import com.example.classmate.data.service.AppointmentChatService
import com.example.classmate.data.service.AppointmentChatServiceImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Message
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

interface AppointmentChatRepository {
    suspend fun sendMessage(message: Message, uri: Uri?, appointmentId: String)
    suspend fun getMessages(appointmentId: String): List<Message?>
    suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit)
    suspend fun getAppointmentById(appointmentId: String): Appointment
}
class AppointmentChatRepositoryImpl(val chatService: AppointmentChatService = AppointmentChatServiceImpl()) : AppointmentChatRepository {

    override suspend fun sendMessage(message: Message, uri: Uri?, appointmentId: String) {
        uri?.let {
            val imageID = UUID.randomUUID().toString()
            message.imageID = imageID
            chatService.uploadImage(it, imageID)
        }
        message.authorID = Firebase.auth.currentUser?.uid ?: ""
        chatService.sendMessage(message, appointmentId)
    }


    override suspend fun getMessages(appointmentId: String): List<Message?> {
        return chatService.getMessages(appointmentId)
    }

    override suspend fun getLiveMessages(appointmentId: String, callback: suspend (Message) -> Unit) {
        chatService.getLiveMessages(appointmentId) { documentSnapshot ->
            val message = documentSnapshot.toObject(Message::class.java)
            message?.let {
                GlobalScope.launch {
                    it.imageURL = it.imageID?.let { id -> chatService.getImageURLByID(id) }
                    it.isMine = it.authorID == Firebase.auth.currentUser?.uid
                    callback(it)
                }
            }
        }
    }


    override suspend fun getAppointmentById(appointmentId: String): Appointment {
        return chatService.getAppointmentById(appointmentId)
    }
}
