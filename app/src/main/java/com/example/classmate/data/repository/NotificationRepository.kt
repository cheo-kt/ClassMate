package com.example.classmate.data.repository

import com.example.classmate.data.service.AppointmentService
import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl

interface NotificationRepository {
}

class NotificationRepositoryImpl(
    val notificationServices : NotificationService = NotificationServiceImpl()
): NotificationRepository {


}