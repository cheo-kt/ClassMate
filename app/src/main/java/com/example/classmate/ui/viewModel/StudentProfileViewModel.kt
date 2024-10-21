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

class StudentProfileViewModel(val repo: StudentRepository = StudentRepositoryImpl()):ViewModel() {
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

}