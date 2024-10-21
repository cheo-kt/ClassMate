package com.example.classmate.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.data.service.StudentServices
import com.example.classmate.data.service.StudentServicesImpl
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentEditProfileViewModel(val services: StudentServices = StudentServicesImpl(), val repo: StudentRepository = StudentRepositoryImpl()):
    ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    fun showStudentInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = repo.getCurrentStudent()
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _student.value = currentUser
                    } else {
                        _student.value = null
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _student.value = null
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }

            }
        }
    }
    fun updateProfilePhoto(uri: Uri, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        var id = _student.value?.id
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (id != null) {
                    services.uploadProfileImage(uri,id)
                }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }

    }
    fun updateStudentProfile(phone: String,name:String,lastname:String,description:String,email:String) {
        viewModelScope.launch {
            val studentId = _student.value?.id
            if (studentId != null) {
                if (name != _student.value?.name){
                    services.updateStudentField(studentId, "name", name)
                }
                if (phone != _student.value?.phone){
                    services.updateStudentField(studentId, "phone", phone)
                }
                if (lastname != _student.value?.lastname){
                    services.updateStudentField(studentId, "lastname", lastname)
                }
                if (description != _student.value?.description){
                    services.updateStudentField(studentId, "description", description)
                }
                if (email != _student.value?.email){
                    services.updateStudentField(studentId, "email", email)
                }
            }


        }
    }

}