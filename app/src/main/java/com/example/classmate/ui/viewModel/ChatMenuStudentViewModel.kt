package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentRepository
import com.example.classmate.data.repository.AppointmentRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatMenuStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl()): ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)


    private val _appointmentsList = MutableLiveData(listOf<Appointment?>())
    val appointmentList: LiveData<List<Appointment?>> get() =_appointmentsList
    val appointmentState = MutableLiveData<Int?>(0)



    private val _image = MutableLiveData<String?>()
    val image : LiveData<String?> get()  = _image

    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
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
                    withContext(Dispatchers.Main) { studentState.value = 3 }
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