package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.AppointmentServiceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


interface  AppointmentRepository {
    suspend fun eliminateAppointment(appointmentId:String,studentId:String,monitorId:String)
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


}