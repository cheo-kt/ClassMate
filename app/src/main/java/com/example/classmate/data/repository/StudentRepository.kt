package com.example.classmate.data.repository

import android.content.Context
import android.net.Uri
import com.example.classmate.domain.model.Student
import com.example.classmate.data.service.StudentServices
import com.example.classmate.data.service.StudentServicesImpl
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

interface StudentRepository {

    suspend fun  createStudent(student: Student)
    suspend fun  getCurrentStudent():Student?
    suspend fun updateStudentPhoto(id: String, imageUri: Uri,context: Context,oldImageID:String): String
    suspend fun updateStudentInformation(id: String, field: String, value: String)
    suspend fun updateStudentImageUrl(id:String,url:String)
    suspend fun getAppointments():List<Appointment?>
    suspend fun getRequestBroadcast():List<RequestBroadcast?>
    suspend fun getStudentImage(imageURL:String):String
    suspend fun getStudentById(id: String): Student?
    suspend fun getRequest():List<Request?>
    suspend fun getAppointmentsUpdate():List<Appointment?>
}


class StudentRepositoryImpl(
    val studentServices: StudentServices = StudentServicesImpl()
): StudentRepository {
    override suspend fun createStudent(student: Student) {
        studentServices.createStudent(student)
    }

    override suspend fun getCurrentStudent(): Student? {
        Firebase.auth.currentUser?.let {
            return studentServices.getStudentById(it.uid)
        } ?: run {
            return null
        }
    }
    override suspend fun updateStudentPhoto(id: String, imageUri: Uri,context: Context,oldImageID:String): String {
        return studentServices.uploadProfileImage(id, imageUri,context,oldImageID)
    }

    override suspend fun updateStudentInformation(id: String, field: String, value: String) {
        studentServices.updateStudentField(id,field, value)
    }

    override suspend fun updateStudentImageUrl(id:String,url: String) {
        studentServices.updateStudentImageUrl(id,url)
    }

    override suspend fun getAppointments(): List<Appointment?> {
        return  studentServices.getAppointments(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun getRequestBroadcast(): List<RequestBroadcast?> {
        return  studentServices.getRequestBroadcast(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun getRequest(): List<Request?> {
        return  studentServices.getRequest(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun getAppointmentsUpdate(): List<Appointment?> {
        return  studentServices.getAppointmentsUpdate(Firebase.auth.currentUser?.uid ?:"")
    }

    override suspend fun getStudentImage(imageURL: String): String {
        return studentServices.getImageDownloadUrl(imageURL)
    }

    override suspend fun getStudentById(id: String): Student? {
        return studentServices.getStudentById(id)
    }
}
