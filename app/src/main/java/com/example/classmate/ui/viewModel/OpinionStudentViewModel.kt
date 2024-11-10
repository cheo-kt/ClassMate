package com.example.classmate.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.classmate.data.repository.MonitorAuthRepository
import com.example.classmate.data.repository.MonitorAuthRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl

class OpinionStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl(),
                              val repo2: MonitorAuthRepository = MonitorAuthRepositoryImpl()): ViewModel()
{

}