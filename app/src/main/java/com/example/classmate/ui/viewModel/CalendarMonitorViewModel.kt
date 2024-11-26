package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarMonitorViewModel(val repo: MonitorRepository = MonitorRepositoryImpl(),val repoAuth: MonitorAuthRepository = MonitorAuthRepositoryImpl()): ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)


    private val _appointmentlist = MutableLiveData(listOf<Appointment?>())
    val appointmentlist: LiveData<List<Appointment?>> get() = _appointmentlist

    private val _image = MutableLiveData<String?>()
    val image : LiveData<String?> get()  = _image

    fun getAppointments() {
        viewModelScope.launch(Dispatchers.IO) {
            val appointments = repo.getAppointments()
            withContext(Dispatchers.Main) {
                _appointmentlist.value = appointments
            }
        }
    }

    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
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
                Log.e("ViewModel", "Error fetching monitor info: ${e.message}")
            }
        }

    }
    fun logOut(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repoAuth.logOut(monitor.value?.id ?: "")
            }catch (ex: FirebaseAuthException){
                ex.printStackTrace()
            }

        }
    }
}