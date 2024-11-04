package com.example.classmate.data.repository

import com.example.classmate.data.service.NotificationService
import com.example.classmate.data.service.NotificationServiceImpl
import com.example.classmate.data.service.RequestService
import com.example.classmate.data.service.RequestServicesImpl

interface RequestRepository {
}
class RequestRRepositoryImpl(
    val requestServices : RequestService = RequestServicesImpl()
): RequestRepository {


}