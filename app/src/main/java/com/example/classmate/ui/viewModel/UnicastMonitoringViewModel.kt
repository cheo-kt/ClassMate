package com.example.classmate.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.RequestRRepositoryImpl
import com.example.classmate.data.repository.RequestRepository
import com.example.classmate.data.repository.StudentAuthRepository

class UnicastMonitoringViewModel (
    val repo: RequestRepository = RequestRRepositoryImpl()
):ViewModel(){

}




