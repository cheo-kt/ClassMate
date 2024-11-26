package com.example.classmate.data.service


import android.util.Log
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


interface StudentAuthService {

    suspend fun  createStudent(email:String, password:String)
    suspend fun loginWithEmailAndPassword(email: String, password: String)
    suspend fun logOut(studentId:String)
    suspend fun checkAuth()
}

class StudentAuthServiceImpl: StudentAuthService {
    override suspend fun createStudent(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }
    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        val result =
            Firebase.firestore.collection("student").whereEqualTo("email", email).get().await()
        if (!result.isEmpty) {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
        }
        else{
            throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no es estudiante")
        }
    }

    override suspend fun logOut(studentId: String) {
        Firebase.auth.signOut()
    }
    override suspend fun checkAuth() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try {
                val docSnapshot = Firebase.firestore.collection("student").document(user.uid).get().await()
                if (!docSnapshot.exists()) {
                    throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no es student")
                }
            } catch (e: Exception) {
                throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no es student")
            }
        } else {
            throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "El usuario no está autenticado")
        }
    }
}