package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.AppointmentServiceImpl


interface  AppointmentRepository {
}
class AppointmentRepositoryImpl(
    val appointmentServices : AppointmentService = AppointmentServiceImpl()
): AppointmentRepository {


}