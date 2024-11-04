package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.RequestRRepositoryImpl
import com.example.classmate.data.repository.RequestRepository
import com.example.classmate.data.repository.StudentAuthRepository
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnicastMonitoringViewModel (
    val repo: RequestRepository = RequestRRepositoryImpl()
):ViewModel(){
    val authState = MutableLiveData(0)
    //0. Idle
    //1. Loading
    //2. Error
    //3. Success
    fun createRequest(request: Request) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 }
            try {
                repo.createRequest(request)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                ex.printStackTrace()
            }
        }
    }



}




