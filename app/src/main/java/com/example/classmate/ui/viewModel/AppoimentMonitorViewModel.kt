package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppoimentMonitorViewModel(val repo: StudentRepository = StudentRepositoryImpl()): ViewModel() {

    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)



    fun showStudentInformation(idStudent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {studentState.value = 1}
            try {
                val currentUser = repo.getStudentById(idStudent)
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _student.value = currentUser
                        withContext(Dispatchers.Main) {studentState.value = 3}
                    } else {
                        _student.value = null
                        withContext(Dispatchers.Main) {studentState.value = 2}
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _student.value = null
                    studentState.value = 2
                    Log.e("ViewModel", "Error fetching monitor info: ${e.message}")
                }

            }
        }
    }

}