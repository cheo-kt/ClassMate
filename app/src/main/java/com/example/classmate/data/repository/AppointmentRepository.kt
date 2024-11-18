package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.AppointmentServiceImpl
import com.example.classmate.domain.model.Appointment
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import java.util.UUID
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


interface  AppointmentRepository {
    suspend fun eliminateAppointment(appointmentId:String,studentId:String,monitorId:String)
    suspend fun createAppoinment(appointment: Appointment)
    suspend fun verifyAppointment()
}
class AppointmentRepositoryImpl(
    val appointmentServices : AppointmentService = AppointmentServiceImpl()
): AppointmentRepository {

    override suspend fun eliminateAppointment(
        appointmentId: String,
        studentId: String,
        monitorId: String
    ) {
        appointmentServices.deleteAppointmentFromMainCollection(appointmentId)
        appointmentServices.deleteAppointmentForMonitor(monitorId,appointmentId)
        appointmentServices.deleteAppointmentForStudent(studentId,appointmentId)
    }

    override suspend fun createAppoinment(appointment: Appointment) {
        val appointmentId = UUID.randomUUID().toString()
        val appointmentWithId = appointment.copy(id = appointmentId)

        appointmentServices.checkForOverlappingRequest(
            Firebase.auth.currentUser?.uid ?: "",
            appointment)

        appointmentServices.createAppointmentInGeneral(appointmentWithId)
        appointmentServices.createAppointmentForStudent(appointmentWithId)
        appointmentServices.createAppointmentForMonitor(appointmentWithId)
    }

    override suspend fun verifyAppointment() {
        val user= Firebase.auth.currentUser!!.uid
        appointmentServices.verifyAppointmentForStudent(user)
    }
}