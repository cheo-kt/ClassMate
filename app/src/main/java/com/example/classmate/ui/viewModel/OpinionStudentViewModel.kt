package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.domain.model.Student
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpinionStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl(),
                              val repo2: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)

    fun calificateMonitor(opinionsAndQualifications: OpinionsAndQualifications, monitorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { studentState.value = 1 }
            try {
                repo2.calificateMonitor(opinionsAndQualifications, monitorId)
                withContext(Dispatchers.Main) { studentState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { studentState.value = 2 }
                ex.printStackTrace()
            }
        }
    }
}