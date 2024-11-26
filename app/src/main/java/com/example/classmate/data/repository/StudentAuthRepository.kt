package com.example.classmate.data.repository

import com.example.classmate.domain.model.Student
import com.example.classmate.data.service.StudentAuthService
import com.example.classmate.data.service.StudentAuthServiceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface StudentAuthRepository {


    suspend fun signup(student: Student, password:String)
    suspend fun signin(email:String, password: String)
    suspend fun logOut(studentId:String)
    suspend fun checkAuth()
}

class AuthRepositoryImpl(
    val authService: StudentAuthService = StudentAuthServiceImpl(),
    val studentRepository: StudentRepository = StudentRepositoryImpl()
) : StudentAuthRepository {
    override suspend fun signup(student: Student, password: String) {
        //1. Registro en modulo de autenticación
        authService.createStudent(student.email, password)
        //2. Obtenemos el UID
        val uid = Firebase.auth.currentUser?.uid
        //3. Crear el usuario en Firestore
        uid?.let {
            student.id = it
            studentRepository.createStudent(student)
        }
      }
    override suspend fun signin(email: String, password: String) {
        authService.loginWithEmailAndPassword(email, password)
    }

    override suspend fun logOut(studentId: String) {
        authService.logOut(studentId)
    }

    override suspend fun checkAuth() {
        authService.checkAuth()
    }
}