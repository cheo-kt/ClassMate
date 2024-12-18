package com.example.classmate.data.repository

import com.example.classmate.data.service.SubjectService
import com.example.classmate.data.service.SubjectServiceImpl
import com.example.classmate.domain.model.Subject

interface SubjectRepository {
    suspend fun getAllSubjects(): List<Subject>
    suspend fun getSubjectById(id: String): Subject?
    suspend fun addMonitorToSubject(monitorId:String,subjectId: String)
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

    override suspend fun addMonitorToSubject(monitorId:String,subjectId: String) {
        subjectService.addMonitorToSubjectCollection(monitorId,subjectId)
    }
}