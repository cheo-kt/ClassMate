package com.example.classmate.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.jvm.Throws

interface MonitorAuthService {
    suspend fun createMonitor(email:String, password:String)
    suspend fun loginWithEmailAndPassword(email: String, password: String)

}

class MonitorAuthServiceImpl: MonitorAuthService {
    override suspend fun createMonitor(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        val result =
            Firebase.firestore.collection("Monitor").whereEqualTo("email", email).get().await()
        if (!result.isEmpty) {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
        }
        else{
            throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no es monitor")
        }
    }
}