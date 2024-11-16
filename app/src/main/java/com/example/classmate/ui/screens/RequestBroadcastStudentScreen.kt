package com.example.classmate.ui.screens



import android.annotation.SuppressLint
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Dialog
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.components.DatePickerDialog
import com.example.classmate.ui.components.TimePickerDialog
import com.example.classmate.ui.viewModel.NotificationViewModel
import com.example.classmate.ui.viewModel.RequestBroadcastStudentViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.Timestamp
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.time.LocalDateTime
import java.time.LocalTime


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestBroadcastStudentScreen(
    navController: NavController,
    requestBroadcastStudentViewmodel: RequestBroadcastStudentViewModel = viewModel()
) {
    val authState by requestBroadcastStudentViewmodel.authState.observeAsState()
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
    val studentObj: Student? by requestBroadcastStudentViewmodel.student.observeAsState(initial = null)
    val subjects by requestBroadcastStudentViewmodel.subjects.observeAsState(emptyList())
    LaunchedEffect(true) {
        requestBroadcastStudentViewmodel.getStudent()
        requestBroadcastStudentViewmodel.getSubject()
    }
    var selectedSubject = remember { mutableStateOf<Subject?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val intialTime by remember { mutableStateOf("") }
    var initialTimeVisibility by remember { mutableStateOf(false) }
    var datePickerVisibility by remember { mutableStateOf(false) }
    var finalTimeVisibility by remember { mutableStateOf(false) }
    val currentDateTime = LocalDateTime.now()



    val today = LocalDate.now()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezadoestudaintes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopStart)
                            .offset(y = 30.dp)
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
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.classmatelogo),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize(1f) // Ajusta el tamaño de la imagen como un porcentaje del contenedor
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
                Text("¡Haz tu solicitud!", style = MaterialTheme.typography.titleLarge,fontWeight = FontWeight.Bold)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Button(onClick = { expanded = !expanded }) {
                        Text("Escoge tu materia")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ){
                        subjects.forEach { subject ->
                            DropdownMenuItem(onClick = {
                                selectedSubject.value = subject
                                expanded = false
                            }) {
                                Text(
                                    text = subject.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }
                    Text(selectedSubject.value?.name?: "", style = MaterialTheme.typography.titleLarge,fontWeight = FontWeight.Bold)
                }
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                            modifier = Modifier.padding(start = 8.dp),fontWeight = FontWeight.Bold
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
                                if(fecha.isNotEmpty()){
                                    initialTimeVisibility = true
                            }
                            },
                    )

                    TimePickerDialog(
                        visible = initialTimeVisibility,
                        timePickerState = timePickerState
                    ) {
                        val selectedHour = timePickerState.hour
                        val selectedMinute = timePickerState.minute

                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)

                        val fechaSeleccionada = fecha
                        val today = LocalDate.now()

                        val fechaParsed = try {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            LocalDate.parse(fechaSeleccionada, formatter)
                        } catch (e: Exception) {
                            null
                        }

                        if (fechaParsed != null && fechaParsed.dayOfMonth == today.dayOfMonth && fechaParsed.year == today.year) {
                            val currentTime = LocalTime.now()
                            if (selectedTime.isAfter(currentTime.plusHours(1))) {
                                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                                horaInicio = formattedTime
                            } else {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Selecciona una hora al menos una hora después de la actual.")
                                }
                            }
                        } else {
                            // Si la fecha no es hoy, simplemente asignamos la hora seleccionada
                            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                            horaInicio = formattedTime
                        }

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
                                if(fecha.isNotEmpty()){
                                    finalTimeVisibility = true
                                }
                            },
                    )


                    TimePickerDialog(
                        visible = finalTimeVisibility,
                        timePickerState = timePickerState
                    ) {
                        val selectedHour = timePickerState.hour
                        val selectedMinute = timePickerState.minute

                        val selectedTime = LocalTime.of(selectedHour, selectedMinute)

                        val fechaSeleccionada = fecha
                        val today = LocalDate.now()
                        val fechaParsed = try {
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            LocalDate.parse(fechaSeleccionada, formatter)
                        } catch (e: Exception) {
                            null
                        }

                        if (fechaParsed != null && fechaParsed.dayOfMonth == today.dayOfMonth && fechaParsed.year == today.year) {
                            val currentTime = LocalTime.now()
                            if (selectedTime.isAfter(currentTime.plusHours(1))) {
                                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                                horafin = formattedTime
                            } else {
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Selecciona una hora al menos una hora después de la actual.")
                                }
                            }
                        } else {
                            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                            horafin = formattedTime
                        }

                        finalTimeVisibility = false
                    }

                    DatePickerDialog(
                        visible = datePickerVisibility,
                        datePickerState = datePickerState
                    ) {
                        datePickerState.selectedDateMillis?.let {
                            val zonedDateTime = Instant.ofEpochMilli(it + 6 * 60 * 60 * 1000).atZone(ZoneId.systemDefault())
                            val selectedDate = LocalDate.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault())

                            if (selectedDate.isBefore(today)) {
                                datePickerVisibility = false
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Debe ser una fecha de hoy o después")
                                }
                            } else {
                                val formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(zonedDateTime)
                                fecha = formattedDate
                                datePickerVisibility = false
                            }
                        }

                    }

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
                            if(selectedSubject.value?.id == null){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Selecciona una materia")
                                }
                            }
                            else if (modalidadSeleccionada.isEmpty()) {
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


                                requestBroadcastStudentViewmodel.createRequest(
                                    RequestBroadcast("", modalidadSeleccionada.toString(),
                                        tipoMonitoria.toString(),
                                        timestampInitial,
                                        timestampFinal,
                                        notas.toString(),
                                        direccion.toString(),
                                        selectedSubject.value?.id.toString(),
                                        selectedSubject.value?.name.toString(),
                                        studentObj?.id ?: " ",
                                        studentObj?.name?:" "
                                        )

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
                snackbarHostState.showSnackbar("Ya existe una cita a esa hora ")
            }
        }
    } else if (authState == 3) {

        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("La solicitud de monitoria enviada correctamente")
            }
        }
        navController.navigate("HomeStudentScreen")

    }


}
