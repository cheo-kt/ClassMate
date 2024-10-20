package com.example.classmate.data.repository

import com.example.classmate.data.service.MonitorAuthService
import com.example.classmate.data.service.MonitorAuthServiceImpl
import com.example.classmate.data.service.StudentAuthService
import com.example.classmate.data.service.StudentAuthServiceImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface MonitorAuthRepository {

    suspend fun signup(monitor: Monitor, password:String)
    suspend fun signin(email:String, password: String)

}
class MonitorAuthRepositoryImpl(
    val authServiceMonitor: MonitorAuthService = MonitorAuthServiceImpl(),
    val monitorRepository: MonitorRepository = MonitorRepositoryImpl()
) : MonitorAuthRepository {
    override suspend fun signup(monitor: Monitor, password: String) {
        //1. Registro en modulo de autenticación
        authServiceMonitor.createMonitor(monitor.email, password)
        //2. Obtenemos el UID
        val uid = Firebase.auth.currentUser?.uid
        //3. Crear el usuario en Firestore
        uid?.let {
            monitor.id = it
            monitorRepository.createMonitor(monitor)
        }
    }
    override suspend fun signin(email: String, password: String) {
        authServiceMonitor.loginWithEmailAndPassword(email, password)
    }
}