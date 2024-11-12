package com.example.classmate.ui.screens

import PredictiveTextField
import androidx.compose.runtime.remember
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.components.CustomTextFieldWithNumericKeyBoard
import com.example.classmate.ui.viewModel.MonitorSignupViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.classmate.R
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import com.example.classmate.domain.model.MonitorSubject

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MonitorSignUpScreen(navController: NavController, monitorSignupViewModel: MonitorSignupViewModel = viewModel()) {

    val authState by monitorSignupViewModel.authState.observeAsState()
    var names by remember { mutableStateOf("") }
    var lastnames by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var SubjectWithPrice = remember { mutableStateListOf<MonitorSubject>() }
    var subj  by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var selectedSubject:Subject? = null
    val snackbarHostState = remember { SnackbarHostState() } //Mensaje emergente
    val scope = rememberCoroutineScope() //Crear una corrutina (Segundo plano)
    val emailRegex =
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex() //Garantizar formato válido de email

    val subjects by monitorSignupViewModel.subjects.observeAsState(emptyList())
    LaunchedEffect(true) {
        monitorSignupViewModel.getSubject()
    }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerpadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezadom),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate("selectMonitorStudent")
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .offset(y = (-25).dp, x = (-45).dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(50.dp),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Registro",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            modifier = Modifier
                                .offset(y = (-25).dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CustomTextField(
                        value = names,
                        onValueChange = { names = it },
                        label = "Nombres"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = lastnames,
                        onValueChange = { lastnames = it },
                        label = "Apellidos"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                       selectedSubject = PredictiveTextField(
                            value = subj,
                            onValueChange = {subj = it},
                            label = "Materias a monitorear",
                            modifier = Modifier.fillMaxWidth(),
                            subjects = subjects
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                selectedSubject?.let { selected ->
                                        if (!SubjectWithPrice.any { it.name == selected.name }) {
                                        SubjectWithPrice.add(
                                            MonitorSubject(
                                                selected.id,
                                                selected.name,
                                                ""
                                            )
                                        )
                                        selectedSubject = null
                                        subj = ""
                                    }
                                }
                            }, modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Add Icon",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                    SubjectWithPrice.forEachIndexed { index, subject ->
                        var price by remember { mutableStateOf("") }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star, contentDescription = "Star",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = subject.name,
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            )
                            CustomTextFieldWithNumericKeyBoard(
                                value = price,
                                onValueChange = { newPrice ->
                                    val updatedSubject = subject.copy(price = newPrice)
                                    SubjectWithPrice[index] = updatedSubject
                                    price = newPrice
                                },
                                label = "Precio/hora",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = {
                                    SubjectWithPrice.removeAt(index)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextFieldWithNumericKeyBoard(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Telefono",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirmar Contraseña",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (names == "" || lastnames == "" || phone == "" || email == "" ||
                                password == "" || confirmPassword == "" || SubjectWithPrice.any() {
                                    it.price == ""
                                }
                            ) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Completa todos los campos")
                                }
                            } else if (SubjectWithPrice.size == 0) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Añade las materias a monitorear")
                                }
                            } else if (!emailRegex.matches(email)) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Correo electronico mal escrito")
                                }
                            } else if (password.length < 6) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Contraseña muy corta")
                                }
                            } else if (password == confirmPassword) {
                                monitorSignupViewModel.signup(
                                    Monitor(
                                        "",
                                        names,
                                        lastnames,
                                        phone,
                                        SubjectWithPrice,
                                        email,
                                        "",
                                        "", 0.0
                                    ),
                                    password
                                )



                            } else {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Las contraseñas no son iguales")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF209619))
                    ) {
                        Text("Registrarse", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Ya tienes una cuenta? ¡Inicia sesión!",
                        modifier = Modifier.clickable {
                            navController.navigate("signing")
                        },
                        color = Color(0xFF209619),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (authState == 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        } else if (authState == 2) {
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Ha ocurrido un error")
                }
            }
        } else if (authState == 3) {
            navController.navigate("HomeMonitorScreen")
        }
    }
}
