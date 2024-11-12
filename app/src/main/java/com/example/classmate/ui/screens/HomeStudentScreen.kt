package com.example.classmate.ui.screens

import PredictiveTextField
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.MonitorSubject
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.HomeStudentViewModel
import com.example.classmate.ui.viewModel.IntroductionStudentViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStudentScreen(navController: NavController, homeStudentViewModel: HomeStudentViewModel = viewModel()) {
    val monitorState by homeStudentViewModel.monitorList.observeAsState()
    val scrollState = rememberScrollState()
    val student: Student? by homeStudentViewModel.student.observeAsState(initial = null)
    var image = student?.photo
    var filter by remember { mutableStateOf("") }
    val studentState by homeStudentViewModel.studentState.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val maxLength = 20
    var search by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val subjectsState by homeStudentViewModel.subjectList.observeAsState()

    LaunchedEffect(true) {
        homeStudentViewModel.getStudent()
        homeStudentViewModel.loadMoreMonitors()
        homeStudentViewModel.getSubjects()
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
                        painter = painterResource(id = R.drawable.encabezadoestudaintes),
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
                                .clickable(onClick = { /* TODO: Acción de ayuda */ })
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
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
                            student?.let {
                                image = it.photo
                                if (studentState == 2) {
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar("Hay problemas para conectarse con el servidor, revise su conexión")
                                    }
                                }
                            }
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
                                navController.navigate("studentProfile")
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Solicitud de monitoria", onClick = {
                                navController.navigate("requestBroadcast?student=${Gson().toJson(student) ?: "No"}")
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                            }, onDismiss = { expanded = false })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "¿Qué necesitas?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        Modifier
                            .width(250.dp)
                            .align(Alignment.Start)
                    ) {
                        Column {
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
                                            .fillMaxWidth()
                                            .height(40.dp)
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
                            Spacer(modifier = Modifier.height(10.dp))
                            subjectsState?.let {
                                PredictiveTextField(
                                    value = search,
                                    onValueChange = { newSearch -> search = newSearch },
                                    label = "Filtrar por materia",
                                    modifier = Modifier.fillMaxWidth(),
                                    subjects = it
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Monitores Destacados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        monitorState?.let { monitors ->
                            var m:List<Monitor?> = if(filter.isNotEmpty()) {
                                monitors.filter {
                                    it?.name!!.startsWith(
                                        filter,
                                        ignoreCase = true
                                    )
                                }
                            } else{
                                monitors
                            }
                            subjectsState?.let {
                                m = if(search.isNotEmpty()) {
                                    m.filter { it2 ->
                                        it.any { it3 ->
                                            it3.monitorsID.contains(it2?.id)
                                        }

                                    }
                                } else{
                                    m
                                }
                            }


                            m.forEach { monitor ->

                                monitor?.subjects?.forEach { subject ->
                                    if(search.isNotEmpty()) {
                                        if(subject.name.lowercase().startsWith(search.lowercase())){
                                            CreateMonitorCard(
                                                monitor = monitor,
                                                subject = subject,
                                                navController = navController,
                                                student = student
                                            )
                                        }
                                    }else{
                                        CreateMonitorCard(
                                            monitor = monitor,
                                            subject = subject,
                                            navController = navController,
                                            student = student
                                        )
                                    }

                                }
                            }
                        }
                    }
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == monitorState?.lastIndex }
                            .collect { isAtBottom ->
                                if (isAtBottom) {
                                    homeStudentViewModel.loadMoreMonitors()
                                }
                            }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF3F21DB)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { navController.navigate("CalendarStudent") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_today),
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
                            .background(color = Color(0xFFCCD0CF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_home),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(4.dp),
                                tint = Color(0xFF3F21DB)
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = {navController.navigate("notificationStudentPrincipal")}) {
                        Icon(
                            painter = painterResource(id = R.drawable.notifications),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                }
            }
        }
        if (studentState == 1) {
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
        } else if (studentState == 2) {
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Ha ocurrido un error")
                }
            }
        }
    }
}

@Composable
fun CreateMonitorCard(monitor:Monitor, subject: MonitorSubject,navController: NavController,student: Student?){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = monitor.photoUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy((-5).dp)
            ) {
                androidx.compose.material3.Text(
                    text = monitor.name,
                    color = Color.Blue,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
                androidx.compose.material3.Text(
                    text = ("Materia: ${subject.name}"),
                    fontSize = 12.sp,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Star",
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = "${monitor.rating}/5",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(50.dp))
                    Text(
                        text = "$" + subject.price,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 5.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigate(
                            "unicastMonitoring?monitor=${Gson().toJson(monitor) ?: "No"}&student=${Gson().toJson(student) ?: "No"}&materia=${Gson().toJson(subject)}"
                        )

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Calendar",
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(
                            "previewMonitorProfile?monitor=${Gson().toJson(monitor) ?: "No"}&student=${Gson().toJson(student) ?: "No"}&materia=${Gson().toJson(subject)}"
                        )

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = "Arrow",
                            modifier = Modifier.size(50.dp),
                        )
                    }
                }
            }
        }
    }
}