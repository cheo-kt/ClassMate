package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatMenuMonitorViewModel(val repo: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {

    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)


    private val _appointmentsList = MutableLiveData<List<Pair<Appointment, Boolean>>>()
    val appointmentList: LiveData<List<Pair<Appointment, Boolean>>> get() = _appointmentsList
    val appointmentState = MutableLiveData<Int?>(0)



    private val _image = MutableLiveData<String?>()
    val image : LiveData<String?> get()  = _image

    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
        }
    }

    fun getAppointments(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { appointmentState.value = 1 }
            try {
                val appointments = repo.getAppointmentsUpdate()
                withContext(Dispatchers.Main) {
                    _appointmentsList.value = appointments
                    withContext(Dispatchers.Main) { monitorState.value = 3 }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _appointmentsList.value = null
                    appointmentState.value = 2
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }

            }
        }
    }

    fun getMonitorImage(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    _image.value = repo.getMonitorImage(imageUrl)
                }
            }catch (e: Exception){
                Log.e("ViewModel", "Error fetching student info: ${e.message}")
            }
        }

    }
}