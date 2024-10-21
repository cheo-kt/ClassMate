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

class StudentEditProfileViewModel( val repo: StudentRepository = StudentRepositoryImpl()):
    ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentPhotoState = MutableLiveData<Int?>(0)
    val studentUpdateInformation = MutableLiveData<Int?>(0)
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
    fun updateStudentPhoto(imageUri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) { studentPhotoState.value = 1 }
            try {
                val studentId = student.value?.id
                if (studentId != null) {
                    val photoUrl = repo.updateStudentPhoto(studentId, imageUri)
                    repo.updateStudentImageUrl(studentId, photoUrl)
                    withContext(Dispatchers.Main) { studentPhotoState.value = 3}
                } else {
                    Log.e("UpdatePhoto", "El ID del estudiante es nulo.")
                    withContext(Dispatchers.Main) { studentPhotoState.value = 2 }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { studentPhotoState.value = 2 }
                Log.e("UpdatePhoto", "Error al actualizar la foto del estudiante: ${e.message}")
            }

        }
    }
    fun updateStudentProfile(phone: String,name:String,lastname:String,description:String,email:String) {
        viewModelScope.launch {
            val studentId = _student.value?.id
            if (studentId != null) {
                if (name != _student.value?.name){
                    repo.updateStudentInformation(studentId,"name",name)
                }
                if (phone != _student.value?.phone){
                    repo.updateStudentInformation(studentId, "phone", phone)
                }
                if (lastname != _student.value?.lastname){
                    repo.updateStudentInformation(studentId, "lastname", lastname)
                }
                if (description != _student.value?.description){
                    repo.updateStudentInformation(studentId, "description", description)
                }
                if (email != _student.value?.email){
                    repo.updateStudentInformation(studentId, "email", email)
                }
            }
        }
    }

}