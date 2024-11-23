package com.example.classmate.data.repository

import android.util.Log
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
    suspend fun establishRecordatoryForMonitor()
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
        try {
            val appointmentId = UUID.randomUUID().toString()
            val appointmentWithId = appointment.copy(id = appointmentId)

            appointmentServices.checkForOverlappingRequest(
                Firebase.auth.currentUser?.uid ?: "",
                appointment
            )

            appointmentServices.createAppointmentInGeneral(appointmentWithId)
            appointmentServices.createAppointmentForStudent(appointmentWithId)
            appointmentServices.createAppointmentForMonitor(appointmentWithId)
            //TODO Llamar a requestRepo.delete -> Eliminar las 3 referencias al request
        }catch (ex:Exception){
            ex.printStackTrace()
            Log.e(">>>", ex.localizedMessage)
        }
    }

    override suspend fun verifyAppointment() {
        val user= Firebase.auth.currentUser!!.uid
        appointmentServices.verifyAppointmentForStudent(user)
        appointmentServices.establishRecordatory(user)
    }
    override suspend fun establishRecordatoryForMonitor(){
        val user= Firebase.auth.currentUser!!.uid
        appointmentServices.establishRecordatoryForMonitor(user)
    }
}