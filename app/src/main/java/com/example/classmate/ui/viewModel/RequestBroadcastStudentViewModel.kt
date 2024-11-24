package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.NotificationRepository
import com.example.classmate.data.repository.NotificationRepositoryImpl
import com.example.classmate.data.repository.RequestBroadcastRepository
import com.example.classmate.data.repository.RequestBroadcastRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.data.repository.SubjectRepository
import com.example.classmate.data.repository.SubjectRepositoryImpl
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RequestBroadcastStudentViewModel(
    val repo: RequestBroadcastRepository = RequestBroadcastRepositoryImpl(),
    val repoSubjects: SubjectRepository = SubjectRepositoryImpl(),
    val repoStudent: StudentRepository = StudentRepositoryImpl(),
    val repo2: NotificationRepository = NotificationRepositoryImpl()
): ViewModel(){
    val authState = MutableLiveData(0)
    val authState2 = MutableLiveData(0)
    //0. Idle
    //1. Loading
    //2. Error
    //3. Success

    //3. Success
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val subjects = MutableLiveData<List<Subject>>(emptyList())
    val studentState = MutableLiveData<Int?>(0)


    fun getSubject() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { }
            try {
                val subjectsList = repoSubjects.getAllSubjects()
                withContext(Dispatchers.Main) {
                    subjects.value = subjectsList
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                }
                ex.printStackTrace()
            }
        }
    }

    fun createRequest( request: RequestBroadcast) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 }
            try {
                repo.createRequestBroadcast(request)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                ex.printStackTrace()
            }
        }
    }

    fun deleteRequest(StudentID:String, subjectID:String,requestID:String ) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState2.value = 1 }
            try {
                repo.eliminateRequestBroadcast(requestID,subjectID,StudentID)
                withContext(Dispatchers.Main) { authState2.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState2.value = 2 }
                ex.printStackTrace()
            }
        }
    }
    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repoStudent.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
            }
        }
    }
    fun createNotification(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 }
            try {
                repo2.createNotification(notification)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                ex.printStackTrace()
            }
        }
    }
}