package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.viewModel.AppoimentMonitorViewModel
import com.example.classmate.ui.viewModel.AppoimentStudentViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun AppoimentMonitorViewScreen(navController: NavController, appointment: String?, appointmentMonitorViewModel: AppoimentMonitorViewModel = viewModel()) {

    val appointmentObj: Appointment = Gson().fromJson(appointment, Appointment::class.java)
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val studentState by appointmentMonitorViewModel.studentState.observeAsState()
    val student: Student? by appointmentMonitorViewModel.student.observeAsState(initial = null)
    var studentID = appointmentObj?.studentId.toString()
    var image = student?.photo

    LaunchedEffect(true) {
        appointmentMonitorViewModel.showStudentInformation(studentID)
    }
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
                    .fillMaxHeight(0.1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezado),
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
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.classmatelogo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la Box (que será un círculo)
                        .clip(CircleShape)
                        .background(Color(0xFFCCD0CF))
                ){
                    student?.let {
                        image = it.photo
                        if (studentState==2){
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Hay problemas para conectarse con el servidor, revise su conexión")
                            }
                        }
                    }
                    Image(
                        modifier = Modifier
                            .size(200.dp) // Tamaño de la imagen
                            .clip(CircleShape) // Hace que la imagen sea circular
                            .size(200.dp)
                            .fillMaxSize()
                        ,
                        contentDescription = null,
                        painter = rememberAsyncImagePainter(image, error = painterResource(R.drawable.botonestudiante)),
                        contentScale = ContentScale.Crop
                    )
                }

                student?.let {
                    Text(text = it.name + " " + it.lastname
                        ,fontSize = 30.sp,
                        fontWeight = FontWeight.Bold)
                } ?: run {
                    Text(text = "NO_NAME" ,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xffCCD0CF))
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Acerca de la solicitud:")
                        Text("- ${appointmentObj.subjectname}")
                        Text("- ${appointmentObj.mode_class}")
                        Text("- ${appointmentObj.type}")

                        val date1 = Date(appointmentObj.dateInitial.toDate().time)
                        val date2 = Date(appointmentObj.dateFinal.toDate().time)
                        val timeFormat2 = SimpleDateFormat("dd/MM/yyyy")
                        val timeFormat1 = SimpleDateFormat("HH:mm")
                        val date = timeFormat2.format(date1)
                        val formattedTimeInitial = timeFormat1.format(date1)
                        val formattedTimeFinal = timeFormat1.format(date2)
                        Text("- $date $formattedTimeInitial-$formattedTimeFinal")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xffCCD0CF))
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("- ${appointmentObj.description}")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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

                    Box(modifier = Modifier.weight(0.1f))
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFF026900), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendario),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp),
                                tint = Color.White
                            )
                        }
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