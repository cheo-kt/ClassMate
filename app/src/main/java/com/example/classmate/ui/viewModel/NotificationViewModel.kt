package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.NotificationRepository
import com.example.classmate.data.repository.NotificationRepositoryImpl
import com.example.classmate.data.repository.RequestRRepositoryImpl
import com.example.classmate.data.repository.RequestRepository
import com.example.classmate.data.repository.StudentAuthRepository
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel (
    val repo: NotificationRepository = NotificationRepositoryImpl()
):ViewModel(){
    val authState2 = MutableLiveData(0)
    //0. Idle
    //1. Loading
    //2. Error
    //3. Success
    fun createNotification(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState2.value = 1 }
            try {
                repo.createNotification(notification)
                withContext(Dispatchers.Main) { authState2.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState2.value = 2 }
                ex.printStackTrace()
            }
        }
    }



}