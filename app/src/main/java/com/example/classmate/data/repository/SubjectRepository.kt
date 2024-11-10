package com.example.classmate.data.repository

import com.example.classmate.data.service.RequestService
import com.example.classmate.data.service.RequestServicesImpl
import com.example.classmate.data.service.SubjectService
import com.example.classmate.data.service.SubjectServiceImpl
import com.example.classmate.domain.model.Subject

interface SubjectRepository {
    suspend fun getAllSubjects(): List<Subject>
    suspend fun getSubjectById(id: String): Subject?
}

class SubjectRepositoryImpl(
    val subjectService: SubjectService = SubjectServiceImpl()
) : SubjectRepository {

    override suspend fun getAllSubjects(): List<Subject> {
        return subjectService.getAllSubjects()
    }

    override suspend fun getSubjectById(id: String): Subject? {
        return subjectService.getSubjectById(id)
    }
}