package com.example.classmate.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.RequestType
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.DatePickerDialog
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.components.RequestBroadcastCard
import com.example.classmate.ui.components.RequestCard
import com.example.classmate.ui.components.TimePickerDialog
import com.example.classmate.ui.viewModel.HomeMonitorViewModel
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.math.sqrt

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMonitorScreen(navController: NavController, homeMonitorViewModel: HomeMonitorViewModel = viewModel()) {
    val requestState by homeMonitorViewModel.broadcastList.observeAsState()
    val monitorState by homeMonitorViewModel.monitorState.observeAsState()
    val  filterrequestState by homeMonitorViewModel.filterSubjectList.observeAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scrollState = rememberScrollState()
    var filter by remember { mutableStateOf("") }
    val monitor:Monitor? by homeMonitorViewModel.monitor.observeAsState(initial = null)
    val image by homeMonitorViewModel.image.observeAsState()
    val requestTypeList by homeMonitorViewModel.requestType.observeAsState()
    if(monitor?.photoUrl?.isNotEmpty() == true){
        monitor?.let { homeMonitorViewModel.getMonitorPhoto(it.photoUrl) }
    }
    var fecha by remember { mutableStateOf("") }
    var fechaFinal by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var isFilterTypeUp = false
    val requestListFilteredByType by homeMonitorViewModel.requestListType.observeAsState()
    var expanded by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }
    var subjectFilteredList by remember { mutableStateOf(emptyList<Subject>()) }
    val maxLength = 20
    var requestTypeListFilter by remember { mutableStateOf(emptyList<RequestType>()) }
    var subjectIdList by remember { mutableStateOf(emptyList<String>()) }
    var filteringType by remember { mutableStateOf("Materia") }
    var buttonMessageType by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val subjectsState by homeMonitorViewModel.subjectList.observeAsState()
    var buttonMessage by remember { mutableStateOf("Materia no seleccionada") }
    var datePickerVisibility by remember { mutableStateOf(false) }
    var datePickerVisibility2 by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val today = LocalDate.now()
    val requestByDateState by homeMonitorViewModel.requestByDate.observeAsState()
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val formattedDateTimeString = currentDateTime.format(formatter)
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val date: Date = simpleDateFormat.parse(formattedDateTimeString)
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    val datePickerState = rememberDatePickerState()
    val datePickerState2 = rememberDatePickerState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect (navBackStackEntry){
        homeMonitorViewModel.getMonitor()
        val job = homeMonitorViewModel.getMonitor()
        job.join()
        homeMonitorViewModel.getSubjectsList()

    }
    LaunchedEffect(true) {
        val job = homeMonitorViewModel.getMonitor()
        job.join()
        monitor?.let { homeMonitorViewModel.loadMoreRequestB(it) }
        homeMonitorViewModel.getSubjectsList()
        homeMonitorViewModel.getRequesTypeList()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        painter = painterResource(id = R.drawable.encabezado),
                        contentDescription = "Encabezado",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(id = R.drawable.classmatelogo),
                        contentDescription = "classMateLogo",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 2.dp, top = 0.dp)
                            .width(200.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp, top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = { navController.navigate("helpMonitor") })
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = { navController.navigate("notificationMonitorScreen") })
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notifications),
                                contentDescription = "notificaciones",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        // Botón de foto de perfil
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                painter = rememberAsyncImagePainter(
                                    image,
                                    error = painterResource(R.drawable.botonestudiante)
                                ),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color(0xFFCCD0CF))
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                        ) {
                            DropdownMenuItemWithSeparator("Tu perfil", onClick = {
                                navController.navigate("monitorProfile")
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                                homeMonitorViewModel.logOut()
                                navController.navigate("signing")
                            }, onDismiss = { expanded = false })
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "¿A quién vamos a ayudar?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                BasicTextField(
                                    value = filter,
                                    onValueChange = {
                                        if (it.length <= maxLength) {
                                            filter = it
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(150.dp)
                                                .background(
                                                    Color.LightGray,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .border(2.dp, Color.Black, RoundedCornerShape(50))
                                                .padding(horizontal = 15.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (filter.isEmpty()) {
                                                Text(
                                                    text = "Filtrar",
                                                    color = Color.Gray,
                                                )
                                            }
                                            innerTextField()
                                            Icon(
                                                painter = painterResource(id = R.drawable.data_loss_prevention),
                                                contentDescription = "Search Icon",
                                                tint = Color.Black,
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .align(Alignment.CenterEnd)
                                            )
                                        }
                                    },
                                    singleLine = true,
                                )
                                Box {

                                    Button(
                                        onClick = { expandedFilter = true },
                                        colors = ButtonDefaults.buttonColors(
                                            Color(0xFF209619),
                                            Color.White
                                        )
                                    ) {
                                        Row {
                                            androidx.compose.material3.Text(filteringType)
                                            Icon(
                                                imageVector = Icons.Filled.KeyboardArrowDown,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                    androidx.compose.material3.DropdownMenu(
                                        expanded = expandedFilter,
                                        onDismissRequest = { expandedFilter = false },
                                        modifier = Modifier
                                            .background(Color(0xFFCCD0CF))
                                            .border(1.dp, Color.Black)
                                            .width(100.dp)
                                    ) {

                                        DropdownMenuItemWithSeparator("Materia", onClick = {
                                            filteringType = "Materia"
                                            expandedFilter = false
                                        }, onDismiss = { expandedFilter = false })
                                        DropdownMenuItemWithSeparator("Fecha", onClick = {
                                            filteringType = "Fecha"
                                            expandedFilter = false
                                        }, onDismiss = { expandedFilter = false })
                                        DropdownMenuItemWithSeparator("Tipo", onClick = {
                                            filteringType = "Tipo"
                                            expandedFilter = false

                                        }, onDismiss = { expandedFilter = false })
                                    }

                                }
                                IconButton(onClick = {

                                    if (filteringType == "Fecha") {
                                        // homeMonitorViewModel.monitorsFilteredByName(filter)
                                    } else if (filteringType == "Materia") {
                                        if (fecha.isEmpty() || horaInicio.isEmpty() ) {
                                            scope.launch {
                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                snackbarHostState.showSnackbar("la fecha u hora no se han definido.")
                                            }
                                        }else{




                                        }
                                        if( buttonMessage != "Materia no seleccionada") {

                                            homeMonitorViewModel.monitorsFilteredBySubject(
                                                buttonMessage
                                            )
                                        }

                                    } else if (filteringType == "Tipo") {
                                        if(buttonMessageType != "Tipo no seleccionado") {
                                            monitor?.let {
                                                homeMonitorViewModel.getRequestByType(buttonMessageType,
                                                    it
                                                )
                                            }
                                        }
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.data_loss_prevention),
                                        contentDescription = "Search Icon",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF209619))
                                            .padding(10.dp)
                                    )
                                }
                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    if (filteringType == "Materia") {
                        Row {
                            Spacer(modifier = Modifier.width(2.dp))
                            Button(
                                onClick = {

                                    buttonMessageType = "Materia no seleccionada"
                                }, colors = ButtonDefaults.buttonColors(
                                    Color(0xFF209619),
                                    Color.White
                                ),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Row {
                                    androidx.compose.material3.Text(text = buttonMessage)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Search Icon",
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Spacer(modifier = Modifier.height(2.dp))

                                if (filter.isNotEmpty()) {
                                    subjectFilteredList = subjectsState?.filter {
                                        it.name.lowercase().startsWith(filter.lowercase())
                                    } ?: emptyList()
                                }

                                if (subjectFilteredList.isNotEmpty() && filter.isNotEmpty()) {
                                    subjectFilteredList.forEach {
                                        Button(
                                            onClick = {
                                                subjectIdList = it.monitorsID
                                                buttonMessage = it.name

                                            }, colors = ButtonDefaults.buttonColors(
                                                Color(0xFF209619),
                                                Color.White
                                            ),
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                        ) {
                                            androidx.compose.material3.Text(text = it.name)
                                        }
                                    }
                                } else if (filter.isEmpty() || subjectFilteredList.isEmpty()) {
                                    subjectsState?.forEach {

                                        Button(
                                            onClick = {
                                                subjectIdList = it.monitorsID
                                                buttonMessage = it.name

                                            }, colors = ButtonDefaults.buttonColors(
                                                Color(0xFF209619),
                                                Color.White
                                            ),
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                        ) {
                                            androidx.compose.material3.Text(text = it.name)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                    if(filteringType=="Tipo"){
                        Row {
                            Spacer(modifier = Modifier.width(2.dp))
                            Button(
                                onClick = {
                                    isFilterTypeUp = false
                                    buttonMessageType = "Tipo no seleccionado"
                                }, colors = ButtonDefaults.buttonColors(
                                    Color(0xFF209619),
                                    Color.White
                                ),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Row {
                                    androidx.compose.material3.Text(text = buttonMessageType)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Search Icon",
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                .clip(RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Spacer(modifier = Modifier.height(2.dp))

                                if (filter.isNotEmpty()) {
                                    requestTypeListFilter = requestTypeList?.filter {
                                        it.name.lowercase().startsWith(filter.lowercase())
                                    } ?: emptyList()
                                }

                                if (requestTypeListFilter.isNotEmpty() && filter.isNotEmpty()) {
                                    requestTypeListFilter.forEach {
                                        Button(
                                            onClick = {
                                                buttonMessageType = it.name
                                                isFilterTypeUp = true
                                            }, colors = ButtonDefaults.buttonColors(
                                                Color(0xFF209619),
                                                Color.White
                                            ),
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                        ) {
                                            androidx.compose.material3.Text(text = it.name)
                                        }
                                    }
                                } else if (filter.isEmpty() || requestTypeListFilter.isEmpty()) {
                                    requestTypeList?.forEach {

                                        Button(
                                            onClick = {
                                                buttonMessageType = it.name
                                                isFilterTypeUp = true
                                            }, colors = ButtonDefaults.buttonColors(
                                                Color(0xFF209619),
                                                Color.White
                                            ),
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                        ) {
                                            androidx.compose.material3.Text(text = it.name)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }else if(filteringType == "Fecha"){

                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                    .clip(RoundedCornerShape(16.dp))
                            ) {

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
                                        label = { androidx.compose.material3.Text("Limite inferior") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                datePickerVisibility = true
                                            },
                                    )
                                    OutlinedTextField(
                                        value = fechaFinal,
                                        enabled = false,
                                        onValueChange = {},
                                        colors = OutlinedTextFieldDefaults.colors(
                                            disabledTextColor = Color.Black, // Color del texto cuando está deshabilitado
                                            disabledBorderColor = Color.Black, // Color del borde cuando está deshabilitado
                                            disabledLabelColor = Color.Black // Color de la etiqueta cuando está deshabilitada
                                        ),
                                        label = { androidx.compose.material3.Text("Limite superior") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                datePickerVisibility2 = true
                                            },
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally){

                                    Spacer(modifier = Modifier.height(2.dp))
                                    DatePickerDialog(
                                        visible = datePickerVisibility,
                                        datePickerState = datePickerState
                                    ) {
                                        datePickerState.selectedDateMillis?.let {
                                            val zonedDateTime = Instant.ofEpochMilli(it + 6 * 60 * 60 * 1000).atZone(
                                                ZoneId.systemDefault())
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
                                    DatePickerDialog(
                                        visible = datePickerVisibility2,
                                        datePickerState = datePickerState2
                                    ) {
                                        datePickerState2.selectedDateMillis?.let {
                                            val zonedDateTime = Instant.ofEpochMilli(it + 6 * 60 * 60 * 1000).atZone(
                                                ZoneId.systemDefault())
                                            val selectedDate = LocalDate.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault())

                                            if (selectedDate.isBefore(today)) {
                                                datePickerVisibility = false
                                                scope.launch {
                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                    snackbarHostState.showSnackbar("Debe ser una fecha de hoy o después")
                                                }
                                            } else {
                                                val formattedDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(zonedDateTime)
                                                fechaFinal = formattedDate
                                                datePickerVisibility2 = false
                                            }
                                        }

                                    }

                                }


                            }
                            Button(
                                onClick = {
                                    if (fecha.isBlank()) {
                                        scope.launch {
                                            snackbarHostState.currentSnackbarData?.dismiss()
                                            snackbarHostState.showSnackbar("Por favor selecciona una fecha y hora válidas.")
                                        }
                                        return@Button
                                    }
                                    val datetimeFinalString = "${fechaFinal.trim()} 24:00"
                                    val datetimeInitialString = "${fecha.trim()} 00:00"

                                    try {
                                        val datetimeFinal = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(datetimeFinalString)
                                        val datetimeInitial = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(datetimeInitialString)
                                        val currentDate = Date()

                                        if (datetimeInitial != null && datetimeInitial.after(currentDate)) {
                                            if(datetimeFinal != null && datetimeFinal.after(datetimeInitial)){
                                                val timestampInitial = Timestamp(datetimeInitial)
                                                val timestampFinal = Timestamp(datetimeFinal)
                                                monitor?.let {
                                                    Log.e("ERROR",datetimeInitialString)
                                                    homeMonitorViewModel.getRequestAfter(timestampInitial,timestampFinal ,it)
                                                }
                                            }else{
                                                scope.launch {
                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                    snackbarHostState.showSnackbar("No se puede elegir una fecha anterior a la de el limite inferior en el limite superior.")
                                                }
                                            }

                                        } else {
                                            scope.launch {
                                                snackbarHostState.currentSnackbarData?.dismiss()
                                                snackbarHostState.showSnackbar("No se puede elegir una fecha anterior a la actual.")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        scope.launch {
                                            snackbarHostState.currentSnackbarData?.dismiss()
                                            snackbarHostState.showSnackbar("Formato de fecha y hora inválido.")
                                        }
                                    }
                                }, colors = ButtonDefaults.buttonColors(
                                    Color(0xFF209619),
                                    Color.White
                                ),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ){
                                Text(text = "Buscar", color = Color.White)
                            }
                        }


                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    androidx.compose.material3.Text(
                        text = "Solicitudes Broadcast",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        if(filterrequestState!!.isNotEmpty() && filteringType == "Materia") {
                            filterrequestState?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController = navController
                                )
                            }

                        }else if(filteringType == "Tipo" && requestListFilteredByType?.isNotEmpty() == true && isFilterTypeUp){
                            requestListFilteredByType?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController = navController
                                )
                            }
                        } else if(filteringType == "Fecha" && requestByDateState?.isNotEmpty() == true){
                            requestByDateState?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController = navController
                                )
                            }
                        }else{
                            requestState?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController = navController
                                )
                            }
                        }

                    }
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == requestState?.lastIndex }
                            .collect { isAtBottom ->
                                if (isAtBottom) {
                                    monitor?.let { homeMonitorViewModel.loadMoreRequestB(it) }
                                }
                            }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF209619)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("MonitorRequest") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.people),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFF026900), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ){
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_home),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(2.dp)
                                    .offset(y = -(2.dp)),
                                tint = Color.White
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("CalendarMonitor") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendario),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("chatScreenMonitor") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(2.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
    if (monitorState == 1) {
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
    } else if (monitorState == 2) {
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar("Ha ocurrido un error")
            }
        }
    }
}
