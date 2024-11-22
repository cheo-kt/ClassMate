package com.example.classmate.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classmate.data.repository.MonitorRepository
import com.example.classmate.data.repository.MonitorRepositoryImpl
import com.example.classmate.data.repository.StudentRepository
import com.example.classmate.data.repository.StudentRepositoryImpl
import com.example.classmate.data.repository.SubjectRepository
import com.example.classmate.data.repository.SubjectRepositoryImpl
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeStudentViewModel(val repo: StudentRepository = StudentRepositoryImpl(),val subjectsRepo : SubjectRepository = SubjectRepositoryImpl(),
                            val repoMonitor: MonitorRepository = MonitorRepositoryImpl()): ViewModel() {
    private val _student = MutableLiveData<Student?>(Student())
    val student: LiveData<Student?> get() = _student
    val studentState = MutableLiveData<Int?>(0)
    private val limit = 10
    private var monitor: Monitor? = null
    private val _monitorList = MutableLiveData(listOf<Monitor?>())
    val monitorList: LiveData<List<Monitor?>> get() = _monitorList
    private val _monitorListFiltered = MutableLiveData(listOf<Monitor?>())
    val monitorListFiltered: LiveData<List<Monitor?>> get() = _monitorListFiltered
    private val _subjectList = MutableLiveData(listOf<Subject>())
    val subjectList: LiveData<List<Subject>> get() = _subjectList
    private val _image = MutableLiveData<String?>()
    val image: LiveData<String?> get() = _image
    fun getStudent0() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { studentState.value = 1 }
            try {
                val currentUser = repo.getCurrentStudent()
                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        _student.value = currentUser
                        withContext(Dispatchers.Main) { studentState.value = 3 }
                    } else {
                        _student.value = null
                        withContext(Dispatchers.Main) { studentState.value = 2 }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _student.value = null
                    studentState.value = 2
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }

            }
        }
    }

    fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = repo.getCurrentStudent()
            withContext(Dispatchers.Main) {
                _student.value = me
            }
        }
    }

    fun loadMoreMonitors() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) { studentState.value = 1 }
            try {
                val newMonitors = repoMonitor.getMonitors(limit, monitor)
                withContext(Dispatchers.Main) {
                    if (newMonitors.isNotEmpty()) {
                        _monitorList.value = _monitorList.value.orEmpty() + newMonitors
                        monitor = _monitorList.value?.lastOrNull()
                        viewModelScope.launch(Dispatchers.Main) { studentState.value = 3 }
                    } else {
                        viewModelScope.launch(Dispatchers.Main) { studentState.value = 0 }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) { studentState.value = 2 }
            }
        }
    }

    fun getSubjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val temp = subjectsRepo.getAllSubjects()
            if (temp.isNotEmpty()) {
                fun getSubjects() {
                    viewModelScope.launch(Dispatchers.IO) {
                        withContext(Dispatchers.Main) { studentState.value = 1 }
                        try {
                            val currentUser = repo.getCurrentStudent()
                            withContext(Dispatchers.Main) {
                                if (currentUser != null) {
                                    _student.value = currentUser
                                    withContext(Dispatchers.Main) { studentState.value = 3 }
                                } else {
                                    _student.value = null
                                    withContext(Dispatchers.Main) { studentState.value = 2 }
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                _student.value = null
                                studentState.value = 2
                                Log.e("ViewModel", "Error fetching student info: ${e.message}")
                            }

                        }
                    }
                }
            }
        }
    }

        fun getStudentImage(imageUrl: String) {
            viewModelScope.launch(Dispatchers.IO) {

                try {
                    withContext(Dispatchers.Main) {
                        _image.value = repo.getStudentImage(imageUrl)
                    }

                } catch (e: Exception) {
                    Log.e("ViewModel", "Error fetching student info: ${e.message}")
                }
            }
        }

        fun monitorsFilteredByName(name: String) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { studentState.value = 1 }
                try {
                    withContext(Dispatchers.Main) {
                        _monitorListFiltered.value = repoMonitor.searchMonitor(name)
                        studentState.value = 3
                    }
                } catch (ex: FirebaseAuthException) {
                    withContext(Dispatchers.Main) { studentState.value = 2 }
                    ex.printStackTrace()
                }
            }
        }

        fun refresh() {
            _monitorListFiltered.value = emptyList()
        }

}

