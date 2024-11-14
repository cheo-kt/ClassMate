package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentRepository
import com.example.classmate.data.repository.AppointmentRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Subject
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppointmentViewModel(
    val repo: AppointmentRepository = AppointmentRepositoryImpl()
): ViewModel() {
    val authState = MutableLiveData(0)
    val subjects = MutableLiveData<List<Subject>>(emptyList())

    fun createAppointment(appointment:Appointment) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 }
            try {
                repo.createAppoinment(appointment)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                ex.printStackTrace()
            }
        }
    }
    fun verifyAppointments(): Job = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { authState.value = 1 }
        try {
            repo.verifyAppointment()
            withContext(Dispatchers.Main) { authState.value = 3 }
        } catch (ex: FirebaseAuthException) {
            withContext(Dispatchers.Main) { authState.value = 2 }
            ex.printStackTrace()
        }
    }
}