package com.example.classmate.data.service


import android.net.Uri
import android.util.Log
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface StudentServices {

    suspend fun createStudent(student: Student)
    suspend fun  getStudentById(id:String):Student?
    suspend fun uploadProfileImage(id: String,uri: Uri): String
    suspend fun updateStudentField(id: String, field: String, value: Any)
    suspend fun updateStudentImageUrl(id:String,url: String)
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

    override suspend fun uploadProfileImage(id: String,uri: Uri): String  {
        val storageRef = Firebase.storage.reference.child("images/students/$id.jpg")
        storageRef.putFile(uri).await()
        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun updateStudentField(id: String, field: String, value: Any) {
        Firebase.firestore
            .collection("student")
            .document(id)
            .update(field, value)
            .await()
    }

    override suspend fun updateStudentImageUrl(id:String,url: String) {
        Firebase.firestore
            .collection("student")
            .document(id)
            .update("photo", url)
            .await()
    }

}