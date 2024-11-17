package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitorRequestViewModel (val repoMonitor: MonitorRepository = MonitorRepositoryImpl()
): ViewModel() {

    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)
    private var lastReq: Request? = null
    private val _list = MutableLiveData(listOf<Request?>())
    val list: LiveData<List<Request?>> get() = _list
    private val limit = 10

    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repoMonitor.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
        }
    }
    fun loadMoreRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { monitorState.value = 1 }
            try {
                val newRequests = repoMonitor.getRequest(limit, lastReq)
                withContext(Dispatchers.Main) {
                    if (newRequests.isNotEmpty()) {
                        _list.value = _list.value.orEmpty() + newRequests
                        lastReq= _list.value?.lastOrNull()
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

}