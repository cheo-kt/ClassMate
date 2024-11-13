package com.example.classmate.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentEditProfileViewModel( val repo: StudentRepository = StudentRepositoryImpl()):
    ViewModel() {
    val studentPhotoState = MutableLiveData<Int>(0)

    fun updateStudentPhoto(imageUri: Uri,context: Context,studentID:String,oldImageID:String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) { studentPhotoState.value = 1 }
            try {
                val photoUrl = repo.updateStudentPhoto(studentID, imageUri, context,oldImageID)
                repo.updateStudentImageUrl(studentID, photoUrl)
                withContext(Dispatchers.Main) { studentPhotoState.value = 3 }
            } catch (e: FirebaseAuthException) {
                withContext(Dispatchers.Main) { studentPhotoState.value = 2 }
                Log.e("UpdatePhoto", "Error al actualizar la foto del estudiante: ${e.message}")
                withContext(Dispatchers.Main) { studentPhotoState.value = 0 }
            }

        }
    }
    fun updateStudentProfile(studentID: String,field:String,value:String) {
        viewModelScope.launch {
            try {
               repo.updateStudentInformation(studentID,field,value)
            }catch (e: FirebaseAuthException){
                Log.e("UpdateField", "Error al actualizar informacion del estudiante: ${e.message}")
            }

        }
    }

}