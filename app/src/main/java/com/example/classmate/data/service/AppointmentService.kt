package com.example.classmate.data.service

import android.util.Log
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Type_Notification
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID

interface AppointmentService {
    suspend fun createAppointmentInGeneral(appointment: Appointment)
    suspend fun createAppointmentForStudent(appointment: Appointment)
    suspend fun createAppointmentForMonitor(appointment: Appointment)
    suspend fun verifyAppointmentForStudent(userId:String)
    suspend fun deleteAppointmentFromMainCollection(appointmentId: String)
    suspend fun deleteAppointmentForStudent(studentId: String, appointmentId: String)
    suspend fun deleteAppointmentForMonitor(monitorId: String, appointmentId: String)
    suspend fun checkForOverlappingRequest(userId: String, appointment: Appointment): Appointment?
    suspend fun establishRecordatory(userId: String)
    suspend fun establishRecordatoryForMonitor(userId: String)
}
class AppointmentServiceImpl: AppointmentService {
    val notificationService: NotificationService = NotificationServiceImpl()
    override suspend fun createAppointmentInGeneral(appointment: Appointment) {
        Firebase.firestore.collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
    }

    override suspend fun deleteAppointmentFromMainCollection(appointmentId: String) {
        Firebase.firestore.collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun deleteAppointmentForStudent(studentId: String, appointmentId: String) {
        Firebase.firestore.collection("student")
            .document(studentId)
            .collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun deleteAppointmentForMonitor(monitorId: String, appointmentId: String) {
        Firebase.firestore.collection("Monitor")
            .document(monitorId)
            .collection("appointment")
            .document(appointmentId)
            .delete()
            .await()
    }

    override suspend fun checkForOverlappingRequest(
        userId: String,
        appointment: Appointment
    ): Appointment? {

        val startTime = appointment.dateInitial
        val endTime = appointment.dateFinal
        val subcollections = listOf("appointment")

        for (subcollection in subcollections) {
            val querySnapshot = Firebase.firestore.collection("Monitor")
                .document(userId)
                .collection(subcollection)
                .whereGreaterThan("dateFinal", startTime)
                .whereLessThan("dateInitial", endTime)
                .get()
                .await()
            Log.e(">>>", ">>>"+querySnapshot.documents.size)
            for(doc in querySnapshot.documents){
                Log.e(">>>", ">>>"+doc.toString())
            }
            if (!querySnapshot.isEmpty) {
                return throw FirebaseAuthException("ERROR_USER_NOT_FOUND", "ERROR_USER_NOT_FOUND"+subcollection)
            }
        }
        return null
    }

    override suspend fun establishRecordatory(userId: String) {
        val db = Firebase.firestore
        val now = Timestamp.now()
        val twoDaysAfter = Timestamp.now().toDate().time + 48 * 60 * 60 * 1000
        val result = db.collection("student")
            .document(userId)
            .collection("appointment")
            .whereGreaterThanOrEqualTo("dateFinal", now) //Seleccioname aquellos apointments que se encuentren entre hoy y los proximos 2 dias
            .whereLessThanOrEqualTo("dateFinal", Timestamp(Date(twoDaysAfter)))
            .get()
            .await()

        result.forEach { document ->
            val isNotificationSent = document.getBoolean("NotificationGenerated") ?: false
            if (!isNotificationSent) {
                val appointment = document.toObject(Appointment::class.java)
                notificationService.createNotification(
                    Notification(
                        UUID.randomUUID().toString(),
                        Timestamp.now(),
                        appointment.dateInitial,
                        "¡Recuerda tu cita próxima!",
                        appointment.subjectname,
                        appointment.studentId,
                        appointment.studentName,
                        appointment.monitorId,
                        appointment.monitorName,
                        Type_Notification.RECORDATORIO
                    )
                )
                document.reference.update("NotificationGenerated", true).await()
            }
        }
    }

    override suspend fun establishRecordatoryForMonitor(userId: String) {
        val db = Firebase.firestore
        val now = Timestamp.now()
        val twoDaysAfter = Timestamp.now().toDate().time + 48 * 60 * 60 * 1000
        val result = db.collection("Monitor")
            .document(userId)
            .collection("appointment")
            .whereGreaterThanOrEqualTo("dateFinal", now)
            .whereLessThanOrEqualTo("dateFinal", Timestamp(Date(twoDaysAfter)))
            .get()
            .await()

        result.forEach { document ->
            val isNotificationSent = document.getBoolean("NotificationGenerated") ?: false
            if (!isNotificationSent) {
                val appointment = document.toObject(Appointment::class.java)
                notificationService.createNotification(
                    Notification(
                        UUID.randomUUID().toString(),
                        Timestamp.now(),
                        appointment.dateInitial,
                        "¡Recuerda tu cita próxima!",
                        appointment.subjectname,
                        appointment.studentId,
                        appointment.studentName,
                        appointment.monitorId,
                        appointment.monitorName,
                        Type_Notification.RECORDATORIO
                    )
                )
                document.reference.update("NotificationGenerated", true).await()
            }
        }
    }

    override suspend fun createAppointmentForStudent(appointment: Appointment) {
        Firebase.firestore.collection("student")
            .document(appointment.studentId)
            .collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()

    }

    override suspend fun createAppointmentForMonitor(appointment: Appointment) {
        Firebase.firestore.collection("Monitor")
            .document(appointment.monitorId)
            .collection("appointment")
            .document(appointment.id)
            .set(appointment)
            .await()
    }

    override suspend fun verifyAppointmentForStudent(userId: String) {
        val db = Firebase.firestore
        val result = db.collection("student")
            .document(userId)
            .collection("appointment")
            .whereLessThan("dateFinal", Timestamp.now()) //Traeme todo apointment cuya fecha final sea menor a la actual (ya se acabo)
            .get()
            .await()

        result.forEach { document ->
            val isNotificationSent = document.getBoolean("NotificationGenerated") ?: false
            if (!isNotificationSent) {
                val appointment = document.toObject(Appointment::class.java)
                notificationService.createNotificationQualification(
                    Notification(
                        UUID.randomUUID().toString(),
                        Timestamp.now(),
                        appointment.dateInitial,
                        "¿Qué tal tu monitoria?",
                        appointment.subjectname,
                        appointment.studentId,
                        appointment.studentName,
                        appointment.monitorId,
                        appointment.monitorName,
                        Type_Notification.CALIFICACION
                    )
                )
                document.reference.update("NotificationGenerated", true).await()
            }
        }
    }
}
