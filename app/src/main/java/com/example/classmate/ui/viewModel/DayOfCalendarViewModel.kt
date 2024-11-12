package com.example.classmate.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentRepository
import com.example.classmate.data.repository.AppointmentRepositoryImpl
import com.example.classmate.data.repository.RequestBroadcastRepository
import com.example.classmate.data.repository.RequestBroadcastRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.RequestBroadcast
import kotlinx.coroutines.launch

class DayOfCalendarViewModel( val repoAppointment: AppointmentRepository = AppointmentRepositoryImpl()
                              , val repoRequestBroadcast : RequestBroadcastRepository = RequestBroadcastRepositoryImpl()): ViewModel() {

    // Lista mutable de appointments y requests usando LiveData
    private val _appointments = MutableLiveData<List<Appointment>>(emptyList())
    val appointments: LiveData<List<Appointment>> = _appointments

    private val _requests = MutableLiveData<List<RequestBroadcast>>(emptyList())
    val requests: LiveData<List<RequestBroadcast>> = _requests



    fun deleteRequestBroadcast(requestBroadcastId: String, subjectID: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                repoRequestBroadcast.eliminateRequestBroadcast(requestBroadcastId,subjectID)
                _requests.value = _requests.value?.filterNot { it.id == requestBroadcastId }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteAppointment(appointmentId: String, studentId:String, monitorId:String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                repoAppointment.eliminateAppointment(appointmentId,studentId,monitorId)
                _appointments.value = _appointments.value?.filterNot { it.id == appointmentId }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }


    fun loadAppointmentsAndRequests(appointmentsList: List<Appointment>, requestsList: List<RequestBroadcast>) {
        _appointments.value = appointmentsList
        _requests.value = requestsList
    }



}