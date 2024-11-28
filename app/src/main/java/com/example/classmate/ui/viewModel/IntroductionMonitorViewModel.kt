package com.example.classmate.ui.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classmate.R

class IntroductionMonitorViewModel : ViewModel() {


    // Lista de imágenes y mensajes
    private val images = listOf(R.drawable.local_library,  R.drawable.profileburbule, R.drawable.calendario, R.drawable.paid)
    private val messages = listOf(
        "¡Ayuda a los estudiantes con sus materias!",
        "¡Comunicate con ellos de una forma sencilla!",
        "¡Acepta los horarios que más te convengan!",
        "¡Ponle tu propia tarifa a las monitorias!"
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