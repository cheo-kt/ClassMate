package com.example.classmate.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.R
import com.example.classmate.ui.viewModel.UnicastMonitoringViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.NotificationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.Timestamp
import com.google.gson.Gson


@Composable
fun UnicastMonitoringScreen(navController: NavController, monitor:String?, student:String?, materia:String?, unicastMonitoringViewModel: UnicastMonitoringViewModel = viewModel(),notificationViewModel: NotificationViewModel = viewModel()) {
    val authState by unicastMonitoringViewModel.authState.observeAsState()
    val authState2 by notificationViewModel.authState2.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val modalidadSeleccionada = remember { mutableStateOf("") }
    val direccion = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horafin = remember { mutableStateOf("") }
    val notas = remember { mutableStateOf("") }
    val tipoMonitoria = remember { mutableStateOf("") }
    val formatoFechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val fechaHoraString = "${fecha.value} ${horaInicio.value}"
    val studentObj:Student = Gson().fromJson(student, Student::class.java)
    val monitorObj:Monitor = Gson().fromJson(monitor, Monitor::class.java)
    LaunchedEffect(true) {
        Log.e(">>>",student?:"No")
        Log.e(">>>",monitor?:"No")
        Log.e(">>>",materia?:"No")
    }
    //val fechaHoraDate: Date? = formatoFechaHora.parse(fechaHoraString)
    //val fechaHoraTimestamp = fechaHoraDate?.let { Timestamp(it) } ?: Timestamp.now()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Sección superior fija
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.encabezadorequest),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { navController.navigate("monitorProfile") },
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

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.classmatelogo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(250.dp)
                    )
                }
            }

            // Sección desplazable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // Opciones de modalidad (Virtual, Presencial, etc.)
                Text("¡Haz tu solicitud!", style = MaterialTheme.typography.titleLarge)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Opciones de modalidad
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = modalidadSeleccionada.value == "Virtual",
                            onClick = { modalidadSeleccionada.value = "Virtual" }
                        )
                        Text(
                            text = "Virtual",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = modalidadSeleccionada.value == "Presencial",
                            onClick = { modalidadSeleccionada.value = "Presencial" }
                        )
                        Text(
                            text = "Presencial",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        // Campo para ingresar dirección (solo se muestra si es presencial)
                        if (modalidadSeleccionada.value == "Presencial") {
                            OutlinedTextField(
                                value = direccion.value,
                                onValueChange = { nuevaDireccion -> direccion.value = nuevaDireccion },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .width(200.dp),
                                label = { Text("Dirección") }, // Etiqueta para el campo de dirección
                                readOnly = false // Permite que el usuario edite el campo
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoMonitoria.value == "Taller",
                            onClick = { tipoMonitoria.value == "Taller" }
                        )
                        Text(
                            text = "Taller",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoMonitoria.value == "Monitoria",
                            onClick = { tipoMonitoria.value == "Monitoria" }
                        )
                        Text(
                            text = "Monitoria",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoMonitoria.value == "Preparcial",
                            onClick = { tipoMonitoria.value == "Preparcial" }
                        )
                        Text(
                            text = "Preparcial",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = tipoMonitoria.value == "Otro",
                            onClick = { tipoMonitoria.value == "Otro" }
                        )
                        Text(
                            text = "Otro",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }


                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Fecha y hora de solicitud
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CustomTextField(value =fecha.value , onValueChange = {fecha.value = it}, label =  "Fecha (dd/mm/yyyy)")
                    // Selector de fecha

                    Spacer(modifier = Modifier.width(16.dp))
                    CustomTextField(value = horaInicio.value, onValueChange ={horaInicio.value = it}, label = "Hora de inicio (HH:mm)" )
                    // Selector de hora inicio

                    Spacer(modifier = Modifier.width(16.dp))
                    CustomTextField(value = horafin.value, onValueChange ={horafin.value = it}, label = "Hora de fin (HH:mm)" )

                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))



                // Notas
                OutlinedTextField(
                    value = notas.value,
                    onValueChange = { nuevaNota -> notas.value = nuevaNota },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = false // Permite que el usuario edite el campo
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de confirmar o cancelar
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if(modalidadSeleccionada.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("modalidad no seleccionada")
                                }
                            }
                            else if(modalidadSeleccionada.value == "Presencial" && direccion.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("no se ha especificado la dirección")
                                }
                            }
                            else if(fecha.value.isEmpty() || horaInicio.value.isEmpty() || horafin.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("la fecha u hora no se han definido.")
                                }
                            }
//                            else{
//
//                                unicastMonitoringViewModel.createRequest(
//                                    Request("",
//                                        modalidadSeleccionada.toString(),
//                                        tipoMonitoria.toString(),fechaHoraTimestamp,
//                                        notas.toString(),
//                                        direccion.toString(),
//                                        materia.toString(), studentObj.id, studentObj.name,monitorObj.id,monitorObj.name)
//                                )
//
//                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirmar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate("monitorProfile") },
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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

        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("La solicitud de monitoria enviada correctamente")
            }
        }

//        notificationViewModel.createNotification(
//            Notification("",fechaHoraTimestamp,"¡Tienes una nueva solicitud de monitoria!",
//                materia.toString(),studentObj.id,studentObj.name,monitorObj.id,monitorObj.name)
//        )
        if (authState2 == 1) {
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
        } else if (authState2 == 2) {
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Ha ocurrido un error")
                }
            }
        } else if (authState2 == 3) {
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("La notificación fue enviada correctamente")
                }
            }
            navController.navigate("HomeStudentScreen")

        }

    }


}

