package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AppointmentRepository
import com.example.classmate.data.repository.AppointmentRepositoryImpl
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.NotificationRepository
import com.example.classmate.data.repository.NotificationRepositoryImpl
import com.example.classmate.data.repository.RequestBroadcastRepository
import com.example.classmate.data.repository.RequestBroadcastRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationMonitorViewModel(val repo2: MonitorRepository = MonitorRepositoryImpl(), val repo: NotificationRepository = NotificationRepositoryImpl(),
                                   val repo3: AppointmentRepository = AppointmentRepositoryImpl(), val repo4: RequestBroadcastRepository = RequestBroadcastRepositoryImpl(),
                                   val repoAuth: MonitorAuthRepository = MonitorAuthRepositoryImpl()
): ViewModel()  {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)
    private val limit = 10
    private var lastNotification: Notification? = null
    private val _notificationsList = MutableLiveData(listOf<Notification?>())
    val notificationList: LiveData<List<Notification?>> get() = _notificationsList
    private val _image = MutableLiveData<String?>()
    val image : LiveData<String?> get()  = _image

    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo2.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
        }
    }
    fun loadMoreMonitorNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { monitorState.value = 1 }
            try {
                val newNotification = repo.loadMoreNotificationsForMonitor(limit, lastNotification)
                withContext(Dispatchers.Main) {
                    if (newNotification.isNotEmpty()) {
                        _notificationsList.value = _notificationsList.value.orEmpty() + newNotification
                        lastNotification= _notificationsList.value?.lastOrNull()

                        viewModelScope.launch(Dispatchers.Main) { monitorState.value = 3 }
                    }
                    else{
                        viewModelScope.launch(Dispatchers.Main) { monitorState.value = 0 }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) { monitorState.value = 2 }
            }
        }
    }
    fun deleteNotification(notification: Notification): Job = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { monitorState.value = 1 }
        try {
            repo.deleteNotification(notification)
            withContext(Dispatchers.Main) { monitorState.value = 3 }
        } catch (ex: FirebaseAuthException) {
            withContext(Dispatchers.Main) { monitorState.value = 2 }
            ex.printStackTrace()
        }
    }

    fun deleteNotificationById(idNotification: String): Job = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { monitorState.value = 1 }
        try {
            repo.deleteNotificationById(idNotification)
            withContext(Dispatchers.Main) { monitorState.value = 3 }
        } catch (ex: FirebaseAuthException) {
            withContext(Dispatchers.Main) { monitorState.value = 2 }
            ex.printStackTrace()
        }
    }

    fun getMonitorImage(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    _image.value = repo2.getMonitorImage(imageUrl)
                }
            }catch (e: Exception){
                Log.e("ViewModel", "Error fetching monitor info: ${e.message}")
            }
        }

    }
    fun verifyAppointments(): Job = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { monitorState.value = 1 }
        try {
            repo3.establishRecordatoryForMonitor()
            withContext(Dispatchers.Main) { monitorState.value = 3 }
        } catch (ex: FirebaseAuthException) {
            withContext(Dispatchers.Main) { monitorState.value = 2 }
            ex.printStackTrace()
        }
    }
    fun loadRandomSuggestion(monitor: Monitor): Job = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { monitorState.value = 1 }
        try {
            repo4.loadRandomReqBroadcast(monitor)
            withContext(Dispatchers.Main) { monitorState.value = 3 }
        } catch (ex: FirebaseAuthException) {
            withContext(Dispatchers.Main) { monitorState.value = 2 }
            ex.printStackTrace()
        }
    }

    fun logOut(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repoAuth.logOut(monitor.value?.id ?: "")
            }catch (ex: FirebaseAuthException){
                ex.printStackTrace()
            }

        }
    }
}