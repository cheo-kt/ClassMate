package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewMonitorViewModel(val repo: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    private var lastopinion:OpinionsAndQualifications? = null
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorState = MutableLiveData(0)
    val limit = 2
    private val _opinionList = MutableLiveData(listOf<OpinionsAndQualifications?>())
    val opinionList: LiveData<List<OpinionsAndQualifications?>> get() = _opinionList
    private val _image = MutableLiveData<String?>()
    val image: LiveData<String?> get() = _image


    fun getOpinionMonitor(monitorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { monitorState.value = 1 }
            try {
                val newOpinion = repo.loadMoreOpinions(limit, lastopinion,monitorId)
                withContext(Dispatchers.Main) {
                    if (newOpinion.isNotEmpty()) {
                        _opinionList.value = _opinionList.value.orEmpty() + newOpinion
                        lastopinion= _opinionList.value?.lastOrNull()
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
    fun getMonitorPhoto(imageUrl:String){
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _image.value =  repo.getMonitorImage(imageUrl)

            }

        }
    }

}






