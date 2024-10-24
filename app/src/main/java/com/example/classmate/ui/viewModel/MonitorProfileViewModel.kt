package com.example.classmate.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.service.MonitorServices
import com.example.classmate.data.service.MonitorServicesImpl
import com.example.classmate.domain.model.Monitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitorProfileViewModel(val repo: MonitorRepository = MonitorRepositoryImpl()):ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState= MutableLiveData<Int?>(0)
    fun showMonitorInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = repo.getCurrentMonitor()
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _monitor.value = currentUser
                        withContext(Dispatchers.Main){monitorState.value= 3}
                    } else {
                        _monitor.value = null
                        withContext(Dispatchers.Main){monitorState.value= 2}
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _monitor.value = null
                    monitorState.value=2
                    Log.e("ViewModel", "Error fetching monitor info: ${e.message}")
                }

            }
        }
    }

}