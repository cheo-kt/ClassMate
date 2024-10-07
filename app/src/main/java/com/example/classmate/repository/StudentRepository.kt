package com.example.classmate.repository

import com.example.classmate.domain.model.Student
import com.example.classmate.service.StudentServices
import com.example.classmate.service.StudentServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface StudentRepository {



    suspend fun  createStudent(student: Student)
    suspend fun  getCurrentStudent():Student?


}


class StudentRepositoryImpl(
    val studentServices: StudentServices = StudentServicesImpl()
):StudentRepository{
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

}
