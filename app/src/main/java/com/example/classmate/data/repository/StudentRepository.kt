package com.example.classmate.data.repository

import android.content.Context
import android.net.Uri
import com.example.classmate.domain.model.Student
import com.example.classmate.data.service.StudentServices
import com.example.classmate.data.service.StudentServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface StudentRepository {



    suspend fun  createStudent(student: Student)
    suspend fun  getCurrentStudent():Student?
    suspend fun updateStudentPhoto(id: String, imageUri: Uri,context: Context): String
    suspend fun updateStudentInformation(id: String, field: String, value: Any)
    suspend fun updateStudentImageUrl(id:String,url:String)
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
    override suspend fun updateStudentPhoto(id: String, imageUri: Uri,context: Context): String {
        return studentServices.uploadProfileImage(id, imageUri,context)
    }

    override suspend fun updateStudentInformation(id: String, field: String, value: Any) {
        studentServices.updateStudentField(id,field, value)
    }

    override suspend fun updateStudentImageUrl(id:String,url: String) {
        studentServices.updateStudentImageUrl(id,url)
    }
}
