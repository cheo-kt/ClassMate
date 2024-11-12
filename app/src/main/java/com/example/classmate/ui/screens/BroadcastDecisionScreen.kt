package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.AppointmentViewModel
import com.google.gson.Gson

@Composable
fun BroadcastDecisionScreen(
    navController: NavController,
    request: String?,
    monitor: String?,
    appointmentViewModel: AppointmentViewModel = viewModel()
    ) {

    val scope = rememberCoroutineScope()
    val requestObj: RequestBroadcast = Gson().fromJson(request, RequestBroadcast::class.java)
    val monitorObj: Monitor = Gson().fromJson(monitor, Monitor::class.java)
    var image = monitor
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val maxLength = 20
    val listState = rememberLazyListState()

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
                        .height(100.dp)
                        .background(Color(0xFF209619)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(Modifier.align(Alignment.TopStart)) {
                        IconButton(
                            onClick = {
                                navController.navigate("monitorProfile")
                            },
                            modifier = Modifier
                                .size(50.dp)
                                .offset(y = (25.dp))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(50.dp),
                                tint = Color.White
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.classmatelogo),
                            contentDescription = "classMateLogo",
                            modifier = Modifier
                                .padding(start = 2.dp, bottom = 0.dp)
                                .width(150.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 24.dp, top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = { /* TODO: Acción de ayuda */ })
                        ) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .width(50.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.notifications),
                                    contentDescription = "Ayuda",
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .width(50.dp)
                                .offset(y = 5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Column {
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

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(Color(0xFFCCD0CF))
                                        .border(1.dp, Color.Black)
                                        .padding(2.dp)
                                ) {
                                    DropdownMenuItemWithSeparator("Tu perfil", onClick = {
                                        navController.navigate("studentProfile")
                                    }, onDismiss = { expanded = false })

                                    DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                                    }, onDismiss = { expanded = false })
                                }
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.weight(0.1f))
            Column {
                Text(text = requestObj.studentName)
                Text(text = requestObj.subjectname)
                Text(text = "Tipo de ayuda")
                Row {
                    Text(text = "Horarios:")
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(text = requestObj.dateInitial.toDate().toString())
                        Text(text = requestObj.dateFinal.toDate().toString())
                    }
                }
                Row {
                    Text(text = "Presencial/Virtual:")
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = requestObj.place)
                }
                Row {
                    IconButton(
                        onClick = {
                            appointmentViewModel.createAppointment(
                                Appointment(
                                    "",
                                    requestObj.mode_class,
                                    requestObj.type,
                                    requestObj.dateInitial,
                                    requestObj.dateFinal,
                                    requestObj.description,
                                    requestObj.place,
                                    requestObj.subjectID,
                                    requestObj.subjectname,
                                    requestObj.studentId,
                                    requestObj.studentName,
                                    monitorObj.id,
                                    monitorObj.name
                                )
                            )
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
                            contentDescription = "Aceptar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("HomeMonitorScreen") },
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

            Box(modifier = Modifier.weight(0.1f))

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
                    IconButton(onClick = { /*TODO*/ }) {
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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_today),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(100.dp)
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
                                .padding(2.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                }
            }
        }
    }
}