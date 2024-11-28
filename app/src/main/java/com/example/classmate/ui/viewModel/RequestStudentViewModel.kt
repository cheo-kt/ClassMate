package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Monitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RequestStudentViewModel(val repo: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)

    private val _imageMonitor = MutableLiveData<String?>()
    val imageMonitor: LiveData<String?> get() = _imageMonitor

    fun showMonitorInformation(idMonitor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {monitorState.value = 1}
            try {
                val currentUser = repo.getMonitorById(idMonitor)
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _monitor.value = currentUser
                        withContext(Dispatchers.Main) {monitorState.value = 3}
                    } else {
                        _monitor.value = null
                        withContext(Dispatchers.Main) {monitorState.value = 2}
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _monitor.value = null
                    monitorState.value = 2
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }

            }
        }
    }
    fun getMonitorPhoto(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _imageMonitor.value =  repo.getMonitorImage(imageUrl)

            }

        }
    }



}