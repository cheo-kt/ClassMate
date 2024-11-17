package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.NotificationRepository
import com.example.classmate.data.repository.NotificationRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Student
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationStudentViewModel( val repo2: StudentRepository =StudentRepositoryImpl(),val repo: NotificationRepository = NotificationRepositoryImpl()): ViewModel()  {

    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)
    private val limit = 10
    private var lastNotification: Notification? = null
    private val _notificationsList = MutableLiveData(listOf<Notification?>())
    val notificationList: LiveData<List<Notification?>> get() = _notificationsList
    private val _image = MutableLiveData<String?>()
    val image : LiveData<String?> get()  = _image

    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo2.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
            }
        }
    }



    fun loadMoreNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { studentState.value = 1 }
            try {
                val newNotification = repo.loadMoreNotifications(limit, lastNotification)
                withContext(Dispatchers.Main) {
                    if (newNotification.isNotEmpty()) {
                        _notificationsList.value = _notificationsList.value.orEmpty() + newNotification
                        lastNotification= _notificationsList.value?.lastOrNull()
                        viewModelScope.launch(Dispatchers.Main) { studentState.value = 3 }
                    }
                    else{
                        viewModelScope.launch(Dispatchers.Main) { studentState.value = 0 }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) { studentState.value = 2 }
            }
        }
    }
    fun deleteNotification(notification: Notification): Job = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { studentState.value = 1 }
            try {
                repo.deleteNotification(notification)
                withContext(Dispatchers.Main) { studentState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { studentState.value = 2 }
                ex.printStackTrace()
            }
    }
    fun getStudentImage(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main){
                    _image.value = repo2.getStudentImage(imageUrl)
                }

            }catch (e: Exception){
                Log.e("ViewModel", "Error fetching student info: ${e.message}")
            }
        }

    }

}