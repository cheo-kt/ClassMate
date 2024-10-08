package com.example.classmate.data.service


import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


interface StudentServices {

    suspend fun createStudent(student: Student)
    suspend fun  getStudentById(id:String):Student?
}

class StudentServicesImpl: StudentServices {
    override suspend fun createStudent(student: Student) {
        Firebase.firestore
            .collection("student")
            .document(student.id)
            .set(student)
            .await()
    }

    override suspend fun getStudentById(id: String): Student? {
        val user = Firebase.firestore
            .collection("student")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(Student::class.java)
        return userObject
    }

}