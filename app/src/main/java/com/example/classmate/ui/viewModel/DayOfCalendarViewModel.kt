package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentRepository
import com.example.classmate.data.repository.AppointmentRepositoryImpl
import com.example.classmate.data.repository.RequestBroadcastRepository
import com.example.classmate.data.repository.RequestBroadcastRepositoryImpl
import com.example.classmate.data.repository.RequestRRepositoryImpl
import com.example.classmate.data.repository.RequestRepository
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import kotlinx.coroutines.launch

class DayOfCalendarViewModel( val repoAppointment: AppointmentRepository = AppointmentRepositoryImpl()
                              , val repoRequestBroadcast : RequestBroadcastRepository = RequestBroadcastRepositoryImpl(),
                              val repoRequest : RequestRepository = RequestRRepositoryImpl()): ViewModel() {

    // Lista mutable de appointments y requests usando LiveData
    private val _appointments = MutableLiveData<List<Appointment>>(emptyList())
    val appointments: LiveData<List<Appointment>> = _appointments

    private val _requestsBroadcast = MutableLiveData<List<RequestBroadcast>>(emptyList())
    val requestsBroadcast: LiveData<List<RequestBroadcast>> = _requestsBroadcast

    private val _requests = MutableLiveData<List<Request>>(emptyList())
    val requests: LiveData<List<Request>> = _requests



    fun deleteRequestBroadcast(requestBroadcastId: String, subjectID: String,studentId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                repoRequestBroadcast.eliminateRequestBroadcast(requestBroadcastId,subjectID,studentId)
                _requestsBroadcast.value = _requestsBroadcast.value?.filterNot { it.id == requestBroadcastId }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteRequest(requestId: String, studentId:String, monitorId:String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                repoRequest.deleteRequest(studentId,monitorId,requestId)
                _requests.value = _requests.value?.filterNot { it.id == requestId }
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


    fun loadAppointmentsAndRequests(appointmentsList: List<Appointment>, requestsBroadcastList: List<RequestBroadcast>, requestsList: List<Request>) {
        _appointments.value = appointmentsList
        _requestsBroadcast.value = requestsBroadcastList
        _requests.value = requestsList
    }



}