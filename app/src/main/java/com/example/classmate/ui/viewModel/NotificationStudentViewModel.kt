package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.NotificationRepository
import com.example.classmate.data.repository.NotificationRepositoryImpl
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationStudentViewModel(val repo: NotificationRepository = NotificationRepositoryImpl()): ViewModel()  {

    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)
    private val limit = 10
    private var lastNotification: Notification? = null
    private val _notificationsList = MutableLiveData(listOf<Notification?>())
    val notificationList: LiveData<List<Notification?>> get() = _notificationsList

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

}