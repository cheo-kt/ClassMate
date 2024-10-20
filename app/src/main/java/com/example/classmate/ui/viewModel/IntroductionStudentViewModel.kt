package com.example.classmate.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classmate.R

class IntroductionStudentViewModel(): ViewModel() {


    // Lista de imágenes y mensajes
    private val images = listOf(R.drawable.book_2,  R.drawable.add_location_alt, R.drawable.calendar_today, R.drawable.book_3)
    private val messages = listOf(
        "¡Busca la materia que necesites!",
        "¡Ten monitorias en el lugar que quieras!",
        "¡Agenda tu cita donde y cuando quieras!",
        "Cualquier materia de cualquier nivel ¡Solo tienes que buscar!"
    )

    // Estado para la imagen y el mensaje
    val currentImage = MutableLiveData(images[0])
    val currentMessage = MutableLiveData(messages[0])

    private var _currentIndex = 0

    val navigateToNextScreen = MutableLiveData(false)

    // Propiedad pública para acceder al índice actual
    val currentIndex: Int
        get() = _currentIndex

    // Función para cambiar la imagen y el mensaje
    fun changeContent() {
        _currentIndex++
        if (_currentIndex < images.size) {
            currentImage.value = images[_currentIndex]
            currentMessage.value = messages[_currentIndex]
        } else {
            navigateToNextScreen.value = true
        }
    }
}