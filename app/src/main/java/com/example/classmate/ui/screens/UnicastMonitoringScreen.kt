package com.example.classmate.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Dialog
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.MonitorSubject
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.components.DatePickerDialog
import com.example.classmate.ui.components.TimePickerDialog
import com.example.classmate.ui.viewModel.NotificationViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.Timestamp
import com.google.gson.Gson
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnicastMonitoringScreen(
    navController: NavController,
    monitor: String?,
    student: String?,
    materia: String?,
    unicastMonitoringViewModel: UnicastMonitoringViewModel = viewModel(),
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val authState by unicastMonitoringViewModel.authState.observeAsState()
    val authState2 by notificationViewModel.authState2.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var modalidadSeleccionada by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horafin by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var tipoMonitoria by remember { mutableStateOf("") }
    val studentObj: Student = Gson().fromJson(student, Student::class.java)
    val monitorObj: Monitor = Gson().fromJson(monitor, Monitor::class.java)
    val subjectObj: MonitorSubject= Gson().fromJson(materia, MonitorSubject::class.java)

    val intialTime by remember { mutableStateOf("") }
    var initialTimeVisibility by remember { mutableStateOf(false) }
    var datePickerVisibility by remember { mutableStateOf(false) }
    var finalTimeVisibility by remember { mutableStateOf(false) }

    var timestampGlobal by remember { mutableStateOf(Timestamp(Date())) }



    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    val timePickerState2 = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    val datePickerState = rememberDatePickerState(

    )

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
            Row() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),

                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezadorequest),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = (30).dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            modifier = Modifier.size(50.dp),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

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
                            selected = modalidadSeleccionada == "Virtual",
                            onClick = { modalidadSeleccionada = "Virtual" }
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
                            selected = modalidadSeleccionada == "Presencial",
                            onClick = { modalidadSeleccionada = "Presencial" }
                        )
                        Text(
                            text = "Presencial",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        // Campo para ingresar dirección (solo se muestra si es presencial)
                        if (modalidadSeleccionada == "Presencial") {
                            OutlinedTextField(
                                value = direccion,
                                onValueChange = { nuevaDireccion ->
                                    direccion = nuevaDireccion
                                },
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
                            selected = tipoMonitoria == "Taller",
                            onClick = { tipoMonitoria = "Taller" } // Corregido aquí
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
                            selected = tipoMonitoria == "Monitoria",
                            onClick = { tipoMonitoria = "Monitoria" } // Corregido aquí
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
                            selected = tipoMonitoria == "Preparcial",
                            onClick = { tipoMonitoria = "Preparcial" } // Corregido aquí
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
                            selected = tipoMonitoria == "Otro",
                            onClick = { tipoMonitoria = "Otro" } // Corregido aquí
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


                    OutlinedTextField(
                        value = fecha,
                        enabled = false,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black, // Color del texto cuando está deshabilitado
                            disabledBorderColor = Color.Black, // Color del borde cuando está deshabilitado
                            disabledLabelColor = Color.Black // Color de la etiqueta cuando está deshabilitada
                        ),
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                datePickerVisibility = true
                            },
                    )
                    // Selector de fecha

                    Spacer(modifier = Modifier.width(16.dp))


                    OutlinedTextField(
                        value = horaInicio,
                        enabled = false,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black, // Color del texto cuando está deshabilitado
                            disabledBorderColor = Color.Black, // Color del borde cuando está deshabilitado
                            disabledLabelColor = Color.Black // Color de la etiqueta cuando está deshabilitada
                        ),
                        label = { Text("Hora de inicio (HH:mm)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                initialTimeVisibility = true
                            },
                    )

                    TimePickerDialog(
                        visible = initialTimeVisibility,
                        timePickerState = timePickerState
                    ) {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        cal.set(Calendar.MINUTE, timePickerState.minute)
                        val format = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)
                        horaInicio = format
                        initialTimeVisibility = false
                    }


                    Spacer(modifier = Modifier.width(16.dp))


                    OutlinedTextField(
                        value = horafin,
                        enabled = false,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black, // Color del texto cuando está deshabilitado
                            disabledBorderColor = Color.Black, // Color del borde cuando está deshabilitado
                            disabledLabelColor = Color.Black // Color de la etiqueta cuando está deshabilitada
                        ),
                        label = { Text("Hora de fin (HH:mm)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                finalTimeVisibility = true
                            },
                    )

                    TimePickerDialog(
                        visible = finalTimeVisibility,
                        timePickerState = timePickerState2
                    ) {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState2.hour)
                        cal.set(Calendar.MINUTE, timePickerState2.minute)
                        val format = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)
                        horafin = format
                        finalTimeVisibility = false
                    }


                    DatePickerDialog(
                        visible = datePickerVisibility,
                        datePickerState = datePickerState
                    ) {
                        datePickerState.selectedDateMillis?.let {
                            val zonedDateTime = Instant.ofEpochMilli(it+6*60*60*1000).atZone(ZoneId.systemDefault())
                            val formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(zonedDateTime)
                            fecha = formattedDate
                        }
                        datePickerVisibility = false

                    }

                    // Selector de hora inicio

                    Spacer(modifier = Modifier.width(16.dp))
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))


                // Notas
                OutlinedTextField(
                    value = notas,
                    onValueChange = { nuevaNota -> notas = nuevaNota },
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
                            if (modalidadSeleccionada.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("modalidad no seleccionada")
                                }
                            } else if (modalidadSeleccionada == "Presencial" && direccion.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("no se ha especificado la dirección")
                                }

                            }else if(tipoMonitoria.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("no se ha especificado el tipo de monitoria")
                                }

                            } else if (fecha.isEmpty() || horaInicio.isEmpty() || horafin.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("la fecha u hora no se han definido.")
                                }
                            }
                            else{
                                val datetimeInitialString = "${fecha} ${horaInicio}"
                                val datetimeFinalString = "${fecha} ${horafin}"

                                val datetimeInitial = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(datetimeInitialString)
                                val datetimeFinal = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(datetimeFinalString)
                                val timestampInitial = Timestamp(datetimeInitial)
                                val timestampFinal = Timestamp(datetimeFinal)


                                unicastMonitoringViewModel.createRequest(
                                    studentObj.id,
                                    monitorObj.id,

                                    Request("",
                                        modalidadSeleccionada,
                                        tipoMonitoria,timestampInitial,timestampFinal,
                                        notas,
                                        direccion,
                                        subjectObj.subjectId,subjectObj.name,
                                        studentObj.id, studentObj.name,monitorObj.id,monitorObj.name)
                                )

                            }
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

        notificationViewModel.createNotification(
            Notification("",timestampGlobal,"¡Tienes una nueva solicitud de monitoria!",
                subjectObj.name,studentObj.id,studentObj.name,monitorObj.id,monitorObj.name)
        )
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

