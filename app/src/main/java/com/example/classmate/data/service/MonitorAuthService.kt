package com.example.classmate.data.service

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface MonitorAuthService {
    suspend fun createMonitor(email:String, password:String)
    suspend fun loginWithEmailAndPassword(email: String, password: String)

}

class MonitorAuthServiceImpl: MonitorAuthService {
    override suspend fun createMonitor(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

}