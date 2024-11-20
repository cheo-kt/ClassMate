package com.example.classmate.data.service


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID


interface StudentServices {

    suspend fun createStudent(student: Student)
    suspend fun  getStudentById(id:String):Student?
    suspend fun uploadProfileImage(id: String,uri: Uri,context: Context,oldImageID:String): String
    suspend fun updateStudentField(id: String, field: String, value: String)
    suspend fun updateStudentImageUrl(id:String,url: String)
    suspend fun getAppointments(idStudent:String):List<Appointment?>
    suspend fun getRequestBroadcast(idStudent:String):List<RequestBroadcast?>
    suspend fun getRequest(idStudent:String):List<Request?>
    suspend fun getImageDownloadUrl(imageUrl:String):String
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

    override suspend fun uploadProfileImage(id: String,uri: Uri,context: Context,oldImageID:String): String  {
        if(oldImageID.isNotEmpty() && oldImageID != "noImage"){
            Firebase.storage
                .reference.child("images/students/$oldImageID.jpg")
                .delete()
        }
        val uid = UUID.randomUUID()
        val storageRef = Firebase.storage.reference.child("images/students/$uid.jpg")
        val bitmap = withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        }

        val compressedBitmapStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, compressedBitmapStream)

        val compressedData = compressedBitmapStream.toByteArray()

        storageRef.putBytes(compressedData).await()
        return uid.toString()
    }

    override suspend fun updateStudentField(id: String, field: String, value: String) {
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

    override suspend fun getAppointments(idStudent:String):List<Appointment?> {
        return try {
            val appointmentList = Firebase.firestore
                .collection("student")
                .document(idStudent)
                .collection("appointment")
                .get()
                .await()

            appointmentList.documents.map { document ->
                document.toObject(Appointment::class.java)
            }
        } catch (e: Exception) {
            // Si ocurre una excepción, retorna una lista vacía
            emptyList()
        }
    }

    override suspend fun getRequestBroadcast(idStudent:String):List<RequestBroadcast?> {
            val requestBroadcast = Firebase.firestore
                .collection("student")
                .document(idStudent)
                .collection("requestBroadcast")
                .get()
                .await()

            return requestBroadcast.documents.map { document ->
                document.toObject(RequestBroadcast::class.java)
            }

    }
    override suspend fun getRequest(idStudent:String):List<Request?> {
        val request = Firebase.firestore
            .collection("student")
            .document(idStudent)
            .collection("request")
            .get()
            .await()

        return request.documents.map { document ->
            document.toObject(Request::class.java)
        }

    }

    override suspend fun getImageDownloadUrl(imageUrl: String): String {
        return  Firebase.storage.reference
            .child("images")
            .child("students")
            .child("$imageUrl.jpg")
            .downloadUrl
            .await().toString()
    }

}