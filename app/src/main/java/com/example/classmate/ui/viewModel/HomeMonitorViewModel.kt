package com.example.classmate.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeMonitorViewModel(val repoMonitor: MonitorRepository = MonitorRepositoryImpl()
): ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)
    private var broadcastReq: RequestBroadcast? = null
    private var lastReq: RequestBroadcast? = null
    private val _broadcastReqList = MutableLiveData(listOf<RequestBroadcast?>())
    val broadcastList: LiveData<List<RequestBroadcast?>> get() = _broadcastReqList
    private val limit = 10

    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repoMonitor.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
        }
    }
    fun loadMoreRequestB() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { monitorState.value = 1 }
            try {
                val newRequests = repoMonitor.getBroadRequest(limit, broadcastReq)
                withContext(Dispatchers.Main) {
                    if (newRequests.isNotEmpty()) {
                        _broadcastReqList.value = _broadcastReqList.value.orEmpty() + newRequests
                         lastReq= _broadcastReqList.value?.lastOrNull()
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