package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classmate.R
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.ui.viewModel.DayOfCalendarViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DayOfCalendarStudentScreen(navController: NavController, listRequest: List<RequestBroadcast>, listAppointment: List<Appointment>, dayOfCalendarViewModel: DayOfCalendarViewModel= viewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost =
        { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
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

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                 listRequest.forEach { requestBroadcast ->
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp,
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.align(Alignment.CenterVertically),
                                        verticalArrangement = Arrangement.spacedBy((-5).dp)
                                    ) {
                                        androidx.compose.material3.Text(
                                            text = "Solicitud",
                                            color = Color.Blue,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                        androidx.compose.material3.Text(
                                            text = ("Materia: ${requestBroadcast.subjectname}"),
                                            fontSize = 12.sp,
                                        )
                                        val date1 = Date(requestBroadcast.dateInitial.toDate().time)
                                        val date2 = Date(requestBroadcast.dateFinal.toDate().time)
                                        val timeFormat = SimpleDateFormat("HH:mm") // Formato de 24 horas
                                        val formattedTimeInitial = timeFormat.format(date1)
                                        val formattedTimeFinal = timeFormat.format(date2)

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            androidx.compose.material3.Text(
                                                text = (formattedTimeInitial),
                                                fontSize = 12.sp,
                                            )
                                            Text(text = "-")
                                            androidx.compose.material3.Text(
                                                text = (formattedTimeFinal),
                                                fontSize = 12.sp,
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
                                                val jsonRequestBroadcast = Gson().toJson(requestBroadcast)
                                                navController.navigate("requestBroadcastView?requestBroadcast=${jsonRequestBroadcast}")
                                            }){
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
                listAppointment.forEach { appointment ->
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
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                verticalArrangement = Arrangement.spacedBy((-5).dp)
                            ) {
                                androidx.compose.material3.Text(
                                    text = "${appointment.monitorName}",
                                    color = Color.Blue,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                androidx.compose.material3.Text(
                                    text = ("Materia: ${appointment.subjectname}"),
                                    fontSize = 12.sp,
                                )
                                val date1 = Date(appointment.dateInitial.toDate().time)
                                val date2 = Date(appointment.dateFinal.toDate().time)
                                val timeFormat = SimpleDateFormat("HH:mm") // Formato de 24 horas
                                val formattedTimeInitial = timeFormat.format(date1)
                                val formattedTimeFinal = timeFormat.format(date2)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    androidx.compose.material3.Text(
                                        text = (formattedTimeInitial),
                                        fontSize = 12.sp,
                                    )
                                    Text(text = "-")
                                    androidx.compose.material3.Text(
                                        text = (formattedTimeFinal),
                                        fontSize = 12.sp,
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
                                        navController.navigate("requestBroadcastView?requestBroadcast=${Gson().toJson(appointment)?:"No"}")
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
            }
            Box(modifier = Modifier.weight(0.1f))

        }
    }

}