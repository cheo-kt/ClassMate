package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl()): ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)

    private val _appointmentlist = MutableLiveData(listOf<Appointment?>())
    val appointmentlist: LiveData<List<Appointment?>> get() = _appointmentlist
    private val _requestBroadcastlist = MutableLiveData(listOf<RequestBroadcast?>())
    val requestBroadcastlist: LiveData<List<RequestBroadcast?>> get() = _requestBroadcastlist
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
    fun getRequestBroadcast() {
        viewModelScope.launch(Dispatchers.IO) {
                val requestsBroadcast = repo.getRequestBroadcast()
                withContext(Dispatchers.Main) {
                    _requestBroadcastlist.value = requestsBroadcast
                }
        }
    }

    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
            }
        }
    }

    fun getStudentImage(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main){
                    _image.value = repo.getStudentImage(imageUrl)
                }

            }catch (e: Exception){
                Log.e("ViewModel", "Error fetching student info: ${e.message}")
            }
        }

    }

}