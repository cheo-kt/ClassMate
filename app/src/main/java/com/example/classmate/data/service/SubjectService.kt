package com.example.classmate.data.service

import com.example.classmate.domain.model.Subject
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

interface SubjectService {
    suspend fun getAllSubjects(): List<Subject> // Obtener todas las materias del catálogo
    suspend fun getSubjectById(id: String): Subject? // Obtener una materia específica por su ID
    suspend fun addMonitorToSubjectCollection(id:String,subjectId:String)
}
class SubjectServiceImpl : SubjectService {
    private val db = Firebase.firestore

    override suspend fun getAllSubjects(): List<Subject> {
        return  try {

             db.collection("subject")
                .get()
                .await()
                .toObjects(Subject::class.java)

        } catch (e: Exception) {

            emptyList()
        }

    }

    override suspend fun getSubjectById(id: String): Subject? {
        try {
            val subjects = db.collection("subject")
                .document(id)
                .get()
                .await()
                .toObject(Subject::class.java)

            return subjects
        } catch (e: Exception) {
            return null
        }
    }
    override suspend fun addMonitorToSubjectCollection(id: String,subjectId: String) {
        db.collection("subject")
            .document(subjectId)
            .update("monitorsID", FieldValue.arrayUnion(id))
            .await()
    }
}