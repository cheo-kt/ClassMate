package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.AuthRepositoryImpl
import com.example.classmate.data.repository.StudentAuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentSigninViewModel(
    val repo: StudentAuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    val authState = MutableLiveData(0)

    fun signin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { authState.value = 1 }
                repo.signin(email, password)
                withContext(Dispatchers.Main) { authState.value = 3 }
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) { authState.value = 2 }
                delay(500)
                withContext(Dispatchers.Main) { authState.value = 0 }
                ex.printStackTrace()
            }
        }
    }

    fun checkIfLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.checkAuth()
                withContext(Dispatchers.Main) { authState.value = 3 } // Usuario autenticado
            }catch (ex: FirebaseAuthException){
                withContext(Dispatchers.Main) { authState.value = 0 } // No autenticado
            }
        }
    }
}