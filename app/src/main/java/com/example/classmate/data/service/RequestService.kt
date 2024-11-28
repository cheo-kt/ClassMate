package com.example.classmate.data.service

import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.RequestType
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface RequestService {

    suspend fun  createRequest(request: Request)
    suspend fun createRequestForStudent(studentID:String,request:Request)
    suspend fun  createRequestForMonitor(monitorID:String,request:Request)
    suspend fun checkForOverlappingRequest(userId: String, request: Request): Request?
    suspend fun deleteRequestFromMainCollection(requestId: String)
    suspend fun deleteRequestForMonitor(studentId: String, requestId: String)
    suspend fun deleteRequestForStudent(monitorId: String, requestId: String)
    suspend fun getRequestType():List<RequestType>
    suspend fun getRequestByType(type:String,monitorId:String):List<Request>
    suspend fun getRequestByDateRange(timeStampInitial: Timestamp, timeStampFinal: Timestamp, monitor: Monitor): List<Request>
}



class RequestServicesImpl: RequestService {

    override suspend fun createRequest(request: Request) {
        Firebase.firestore
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }
    override suspend fun createRequestForStudent(studentId: String, request: Request) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }
    

    override suspend fun createRequestForMonitor(monitorID: String, request: Request) {
        Firebase.firestore.collection("Monitor")
            .document(monitorID)
            .collection("request")
            .document(request.id)
            .set(request)
            .await()
    }

    // Función para verificar superposición de horarios en las subcolecciones del usuario
    override suspend fun checkForOverlappingRequest(userId: String, request: Request): Request? {
        val startTime = request.dateInitial
        val endTime = request.dateFinal
        val subcollections = listOf("requestBroadcast", "appointment", "request")

        for (subcollection in subcollections) {
            val querySnapshot = Firebase.firestore.collection("student")
                .document(userId)
                .collection(subcollection)
                .whereGreaterThanOrEqualTo("dateFinal", startTime)
                .whereLessThanOrEqualTo("dateInitial", endTime)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                return throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "ya existe ")
            }
        }
        return null
    }

    override suspend fun deleteRequestFromMainCollection(requestId: String) {
        Firebase.firestore.collection("request")
            .document(requestId)
            .delete()
            .await()
    }

    override suspend fun deleteRequestForMonitor(monitorId: String, requestId: String) {
        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("request")
            .document(requestId)
            .delete()
            .await()
    }

    override suspend fun deleteRequestForStudent(studentId: String, requestId: String) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("request")
            .document(requestId)
            .delete()
            .await()
    }

    override suspend fun getRequestType(): List<RequestType> {
        return try{
            Firebase.firestore
                .collection("requestType")
                .get()
                .await()
                .toObjects(RequestType::class.java)
        }catch (e: Exception){
            emptyList<RequestType>()
        }
    }

    override suspend fun getRequestByType(type: String,monitorId:String): List<Request> {
        return  try{
            Firebase.firestore
                .collection("request")
                .whereEqualTo("monitorId",monitorId)
                .whereEqualTo("type",type)
                .get()
                .await()
                .toObjects(Request::class.java)
        }catch (e: Exception){
            emptyList<Request>()
        }
    }

    override suspend fun getRequestByDateRange(
        timeStampInitial: Timestamp,
        timeStampFinal: Timestamp,
        monitor: Monitor
    ): List<Request> {
        val subjects = monitor.subjects.map { it.name }
        return try {
            Firebase.firestore
                .collection("request")
                .whereEqualTo("monitorId",monitor.id)
                .whereIn("subjectname", subjects)
                .whereGreaterThan("dateInitial",timeStampInitial)
                .whereLessThan("dateInitial",timeStampFinal)
                .get()
                .await()
                .toObjects(Request::class.java)
        }catch (e:Exception){
            emptyList<Request>()
        }
    }


}