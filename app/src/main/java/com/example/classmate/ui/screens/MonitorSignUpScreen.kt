package com.example.classmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.MonitorSignupViewModel
import com.example.classmate.ui.viewModel.StudentSignupViewModel

@Composable
fun MonitorSignUpScreen(navController: NavController, monitorSignupViewModel: MonitorSignupViewModel = viewModel()) {

    val authState by monitorSignupViewModel.authState.observeAsState()

    var names by remember { mutableStateOf("") }
    var lastnames by remember { mutableStateOf("") }
    var subjects by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember{mutableStateOf("")}
    var errorMessage by remember { mutableStateOf("") } // Para manejar el mensaje de error

    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registro",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CustomTextField(value = names, onValueChange = { names = it }, label = "Nombres")
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(value = lastnames, onValueChange = {lastnames = it }, label = "Apellidos")
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(value = subjects, onValueChange = { subjects = it }, label = "Seleccione materias a monitorear")
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isPassword = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar Contraseña", isPassword = true)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Mostrar el mensaje de error si las contraseñas no coinciden
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (password == confirmPassword) {
                                    // Si las contraseñas coinciden, proceder con el registro
                                    monitorSignupViewModel.signup(
                                        Monitor("", names, lastnames, phone,subjects, email,""),
                                        password
                                    )
                                } else {
                                    // Si las contraseñas no coinciden, mostrar el mensaje de error
                                    errorMessage = "Las contraseñas no son iguales, intenta de nuevo"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                        ) {
                            Text("Registrarse", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "¿Ya tienes una cuenta? ¡Inicia sesión!",
                            modifier = Modifier.clickable { /* Navegar a la pantalla de login */ },
                            color = Color(0xFF3F51B5),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (authState == 1) {
                CircularProgressIndicator()
            } else if (authState == 2) {
                Text("Hubo un error", color = Color.Red)
            } else if (authState == 3) {
                navController.navigate("profile")
            }
        }
    }
}
