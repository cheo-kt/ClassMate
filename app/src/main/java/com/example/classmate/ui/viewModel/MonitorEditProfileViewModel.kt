package com.example.classmate.ui.viewModel

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitorEditProfileViewModel( val repo: MonitorRepository = MonitorRepositoryImpl()):
    ViewModel() {
    private val _monitor = MutableLiveData<Monitor?>(Monitor())
    val monitor: LiveData<Monitor?> get() = _monitor
    val monitorPhotoState = MutableLiveData<Int?>(0)
    val monitorUpdateInformation = MutableLiveData<Int?>(0)
    fun showMonitorInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = repo.getCurrentMonitor()
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _monitor.value = currentUser
                    } else {
                        _monitor.value = null
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _monitor.value = null
                    Log.e("ViewModel", "Error fetching monitor info: ${e.message}")
                }

            }
        }
    }

    fun updateMonitorPhoto(imageUri: Uri, context: Context,oldImage:String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) { monitorPhotoState.value = 1 }
            try {
                val monitorId = monitor.value?.id
                if (monitorId != null) {
                    val photoUrl =  repo.updateMonitorPhoto(monitorId, imageUri, context, oldImage)
                    repo.updateMonitorImageUrl(monitorId, photoUrl)
                    withContext(Dispatchers.Main) { monitorPhotoState.value = 3 }
                } else {
                    Log.e("UpdatePhoto", "El ID del monitor es nulo.")
                    withContext(Dispatchers.Main) { monitorPhotoState.value = 2 }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { monitorPhotoState.value = 2 }
                Log.e("UpdatePhoto", "Error al actualizar la foto del monitor: ${e.message}")
                withContext(Dispatchers.Main) { monitorPhotoState.value = 0 }
            }

        }
    }

    fun updateMonitorProfile(monitorID:String,field:String,value:String) {
        viewModelScope.launch {
            try {
               repo.updateMonitorInformation(monitorID,field,value)
            } catch (e: FirebaseAuthException) {
                Log.e("UpdateField", "Error al actualizar la información ${e.message}")
            }
        }
    }
}



