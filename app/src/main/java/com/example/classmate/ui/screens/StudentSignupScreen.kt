package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.R

import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.StudentSignupViewModel

@Composable
fun StudentSignupScreen(navController: NavController, studentSignupViewModel: StudentSignupViewModel = viewModel()) {

    val authState by studentSignupViewModel.authState.observeAsState()
    var showPopup by remember { mutableStateOf(false) }

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Para manejar el mensaje de error
    val scrollState = rememberScrollState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cuadroonly),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(

                    text = "Registro",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-25).dp),
                    textAlign = TextAlign.Center
                )

            }
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Nombres",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = nombres,
                            onValueChange = { nombres = it },
                            label = "Escribe tu nombre completo"
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco
                        Text(
                            text = "Apellidos",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = apellidos,
                            onValueChange = { apellidos = it },
                            label = "Escribe tu apellido completo"
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco
                        Text(
                            text = "Telefono",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = "Escribe tu numero de telefono"
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Escribe tu email"
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco
                        Text(
                            text = "Contraseña",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Escribe tu contraseña, minimo 6 caracteres",
                            isPassword = true
                        )
                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco
                        Text(
                            text = "Confirmar Contraseña",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Left
                        )
                        CustomTextField(
                            value = confirmarContrasena,
                            onValueChange = { confirmarContrasena = it },
                            label = "Escribe la misma contraseña",
                            isPassword = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))


                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco

                        Text(text = errorMessage)
                        if(authState == 1){
                            CircularProgressIndicator()
                        }else if(authState == 2){
                            errorMessage =
                                "Registro fallido, intenta de nuevo."

                        }else if (authState == 3){
                            navController.navigate("profile")
                        }

                        Button(
                            onClick = {
                                if (password == confirmarContrasena) {
                                    // Si las contraseñas coinciden, proceder con el registro
                                    studentSignupViewModel.signup(
                                        Student("", nombres, apellidos, telefono, email, ""),
                                        password
                                    )
                                } else {
                                    // Si las contraseñas no coinciden, mostrar el mensaje de error
                                    errorMessage =
                                        "Las contraseñas no son iguales, intenta de nuevo"
                                }
                            },

                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF3F21DB
                                )
                            )
                        ) {
                            Text("Registrarse", color = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .weight(0.1f)
                        ) // Espacio en blanco


                        Text(
                            text = "¿Ya tienes una cuenta? ¡Inicia sesión!",
                            modifier = Modifier.clickable {
                                navController.navigate("signing")
                            },
                            color = Color(0xFF3F21DB),
                            textAlign = TextAlign.Center
                        )
                    }


                }


            }



        }


    }
}

