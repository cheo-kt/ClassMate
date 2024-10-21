package com.example.classmate.ui.screens
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.StudentSigninViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.classmate.R
import com.example.classmate.ui.viewModel.MonitorSigninViewModel
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StudentMonitorSigninScreen (navController: NavController, authViewModel: StudentSigninViewModel = viewModel(),
                                monitorAuthViewModel: MonitorSigninViewModel = viewModel()){


    val authState by authViewModel.authState.observeAsState()
    val authStateMonitor by monitorAuthViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() } //Mensaje emergente
    val scope = rememberCoroutineScope() //Crear una corrutina (Segundo plano)


    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {  innerpadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding)
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center

            ) {
                Image(
                    painter = painterResource(id = R.drawable.sssss),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.classmatelogo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(350.dp)
                        .offset(y = (-50).dp),
                )
            }

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 50.sp, fontWeight =FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier
                .weight(0.1f))

            Box(modifier = Modifier.padding(horizontal = 32.dp)){
                Column {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp, fontWeight =FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                    CustomTextField(value = email, onValueChange = { email = it }, label = "")
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp, fontWeight =FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                    CustomTextField(value = password, onValueChange = { password = it }, label = "", isPassword = true)
                }
            }

            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco
            Box(modifier = Modifier.padding(horizontal = 32.dp)){
                Column {
                    Text(
                        text = "Acceder como:",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Left
                    )
                    Button(
                        onClick = {
                            if (email =="" ||password == "") {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Completa todos los campos")
                                }
                            }
                            else{
                                monitorAuthViewModel.signin(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF209619))
                    ) {
                        Text("Monitor", color = Color.White)
                    }
                    Button(
                        onClick = {

                            if (email =="" ||password == "") {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Completa todos los campos")
                                }
                            }
                            else{
                                authViewModel.signin(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text("Estudiante", color = Color.White)
                    }

                    if(authStateMonitor ==1 || authState == 1){
                        CircularProgressIndicator()
                    }else if(authStateMonitor ==2 ||authState == 2){
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Tu contraseña o tu correo no coincide, intenta de nuevo.")
                        }
                    }else if (authStateMonitor ==3 ||authState == 3){
                        if(authStateMonitor ==3 ){
                            navController.navigate("HomeMonitorScreen")
                        }else {
                            navController.navigate("monitorProfile")
                        }

                    }
                }
            }
            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco
            Text(
                text = "¿Aún no tienes cuenta? ¡Regístrate!",
                modifier = Modifier.clickable {navController.navigate("selectMonitorStudent") },
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )

        }

    }

}




