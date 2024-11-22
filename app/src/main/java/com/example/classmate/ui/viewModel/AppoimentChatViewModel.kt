package com.example.classmate.ui.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentChatRepository
import com.example.classmate.data.repository.AppointmentChatRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AppoimentChatViewModel ( val chatRepository: AppointmentChatRepository = AppointmentChatRepositoryImpl()) : ViewModel() {


    private val _messagesState = MutableLiveData<List<Message?>>()
    val messagesState: LiveData<List<Message?>> get() = _messagesState

    private val _appointmentState = MutableLiveData<Appointment>()
    val appointmentState: LiveData<Appointment> get() = _appointmentState

    fun getAppointment(appointmentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val appointment = chatRepository.getAppointmentById(appointmentId)
            withContext(Dispatchers.Main) {
                _appointmentState.value = appointment
            }
        }
    }

    fun getMessages(appointmentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = chatRepository.getMessages(appointmentId)
            withContext(Dispatchers.Main) { _messagesState.value = messages }
        }
    }

    fun sendMessage(content: String, uri: Uri?, appointmentId: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            content = content
        )
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessage(message, uri, appointmentId)
        }
    }

    fun getMessagesLiveMode(appointmentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getLiveMessages(appointmentId) { message ->
                val currentMessages = _messagesState.value ?: emptyList()
                val updatedMessages = ArrayList(currentMessages)
                updatedMessages.add(message)
                withContext(Dispatchers.Main) { _messagesState.value = updatedMessages }
            }
        }
    }
}