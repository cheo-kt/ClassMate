package com.example.classmate.service


import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


interface StudentAuthService {

    suspend fun  createStudent(email:String, password:String)
    suspend fun loginWithEmailAndPassword(email: String, password: String)

}



class StudentAuthServiceImpl: StudentAuthService{
    override suspend fun createStudent(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }
    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

}