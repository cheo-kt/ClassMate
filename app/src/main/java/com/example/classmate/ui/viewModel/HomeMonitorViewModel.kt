package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.SubjectRepository
import com.example.classmate.data.repository.SubjectRepositoryImpl
import com.example.classmate.data.repository.StudentAuthRepository
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Subject
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeMonitorViewModel(val repoMonitor: MonitorRepository = MonitorRepositoryImpl(),val repoAuth: MonitorAuthRepository = MonitorAuthRepositoryImpl(),
                           val subjectsRepo : SubjectRepository = SubjectRepositoryImpl()
): ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)
    private var lastReq: RequestBroadcast? = null
    private val _broadcastReqList = MutableLiveData(listOf<RequestBroadcast?>())
    val broadcastList: LiveData<List<RequestBroadcast?>> get() = _broadcastReqList
    private val limit = 10
    private val _subjectList = MutableLiveData(listOf<Subject>())
    val subjectList: LiveData<List<Subject>> get() = _subjectList

    fun getMonitor(): Job = viewModelScope.launch(Dispatchers.IO) {
            val me = repoMonitor.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
    }
    fun loadMoreRequestB(monitor: Monitor) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { monitorState.value = 1 }
            try {
                val newRequests = repoMonitor.getBroadRequest(limit, lastReq, monitor)
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
    fun getSubjectsList(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                withContext(Dispatchers.Main) {
                    _subjectList.value = subjectsRepo.getAllSubjects()
                }
            }catch (ex: FirebaseAuthException){
                ex.printStackTrace()
            }
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