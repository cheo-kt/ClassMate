package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.RequestBroadcastRepository
import com.example.classmate.data.repository.RequestBroadcastRepositoryImpl
import com.example.classmate.data.repository.RequestRRepositoryImpl
import com.example.classmate.data.repository.RequestRepository
import com.example.classmate.data.repository.SubjectRepository
import com.example.classmate.data.repository.SubjectRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.RequestType
import com.google.firebase.auth.FirebaseAuthException
import com.example.classmate.domain.model.Subject
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitorRequestViewModel (val repoMonitor: MonitorRepository = MonitorRepositoryImpl(),val repoAuth: MonitorAuthRepository = MonitorAuthRepositoryImpl()
                               ,val subjectsRepo : SubjectRepository = SubjectRepositoryImpl(),val requestRepo : RequestRepository = RequestRRepositoryImpl()
): ViewModel() {

    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData<Int?>(0)
    private var lastReq: Request? = null
    private val _list = MutableLiveData(listOf<Request?>())
    val list: LiveData<List<Request?>> get() = _list
    private val limit = 10
    private val _subjectList = MutableLiveData(listOf<Subject>())
    val subjectList: LiveData<List<Subject>> get() = _subjectList
    private val _filterSubjectList = MutableLiveData(listOf<Request?>())
    val filterSubjectList: LiveData<List<Request?>> get() = _filterSubjectList
    private val _image = MutableLiveData<String?>()
    val image: LiveData<String?> get() = _image
    private val _requestType = MutableLiveData(listOf<RequestType>())
    val requestType: LiveData<List<RequestType>> get() = _requestType
    private val _requestListType = MutableLiveData(listOf<Request?>())
    val requestListType: LiveData<List<Request?>> get() = _requestListType
    private val _requestByDate = MutableLiveData(listOf<Request?>())
    val requestByDate: LiveData<List<Request?>> get() = _requestByDate
    fun getMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repoMonitor.getCurrentMonitor()
            withContext(Dispatchers.Main) {
                _monitor.value = me
            }
        }
    }

    fun monitorsFilteredBySubject(name: String) {
        Log.e(">>>", name)
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { monitorState.value = 1 }
            try {
                withContext(Dispatchers.Main) {
                    _filterSubjectList.value = repoMonitor.searchSubjectsByNameRequest(name)
                    monitorState.value = 3
                }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { monitorState.value = 2 }
                ex.printStackTrace()
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
    fun getMonitorPhoto(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _image.value =  repoMonitor.getMonitorImage(imageUrl)

            }

        }
    }
    fun getRequesTypeList(){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                _requestType.value = requestRepo.getRequestType()
            }
        }
    }
    fun getRequestByType(type:String,monitorId:String){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
               _requestListType.value = requestRepo.getRequestByType(type,monitorId)
            }
        }

    }
    fun getRequestByDateRange(timestampInitial: Timestamp,timestampFinal: Timestamp,monitor: Monitor){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                _requestByDate.value = requestRepo.getRequestByDateRange(timestampInitial,timestampFinal,monitor)
            }
        }
    }

    fun refresh() {
        _filterSubjectList.value = emptyList()
    }

}