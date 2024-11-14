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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.MonitorSubject
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.components.FormatterDate
import com.example.classmate.ui.viewModel.AppointmentViewModel
import com.example.classmate.ui.viewModel.NotificationStudentViewModel
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun NotificationStudentScreen(navController: NavController,
                              notificationStudentViewModel: NotificationStudentViewModel= viewModel(),
                              appointmentViewModel: AppointmentViewModel = viewModel() ) {

    var expanded by remember { mutableStateOf(false) }
    val student: Student? by notificationStudentViewModel.student.observeAsState(initial = null)
    val studentState by notificationStudentViewModel.studentState.observeAsState()
    val notificationState by notificationStudentViewModel.notificationList.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    var image = student?.photo
    var notification = remember { mutableStateListOf<Notification>() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(true) {
        val job = appointmentViewModel.verifyAppointments()
        job.join()
        notificationStudentViewModel.loadMoreNotifications()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(Color(0xFF3F21DB)),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.classmatelogo),
                        contentDescription = "classMateLogo",
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(Color.Transparent)
                            .clickable(onClick = {navController.navigate("HelpStudent") })
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.live_help),
                            contentDescription = "Ayuda",
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.1f))


                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(70.dp)
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
                                contentDescription = "foto de perfil",
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
                                navController.navigate(
                                    "requestBroadcast?student=${
                                        Gson().toJson(
                                            student
                                        ) ?: "No"
                                    }"
                                )
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                            }, onDismiss = { expanded = false })
                        }
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFCCD0CF))
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement =Arrangement.Center
            ) {
                Text(
                    text = "Tus Notificaciones",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                    notificationState?.let { notification ->
                            if (notification.isEmpty()) {
                                Column {
                                    Box(modifier = Modifier.height(100.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.notifications_off),
                                            contentDescription = "Sin notificaciones",
                                            modifier = Modifier.size(200.dp).offset(x = 30.dp)
                                        )
                                    }
                                    Text(
                                        text = "No tienes notificaciones",
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                        }else{
                            LazyColumn(state = listState) {
                        notification.forEachIndexed{ _, n ->
                            item{
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp,
                                ), modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                                    .clickable {
                                        navController.navigate(
                                            "OpinionStudent?notification=${Gson().toJson(n) ?: "No"}"
                                        )
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = R.drawable.botonestudiante,
                                        contentDescription = "a",
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
                                            text = n!!.content,
                                            fontSize = 12.sp,
                                            color = Color.Blue,
                                        )
                                        androidx.compose.material3.Text(
                                            text = n.monitorName,
                                            fontSize = 15.sp,
                                        )
                                        androidx.compose.material3.Text(
                                            text = n.subject,
                                            fontSize = 12.sp,
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .padding(horizontal = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = FormatterDate(timestamp = n!!.date),
                                                fontSize = 16.sp,
                                            )
                                            Spacer(modifier = Modifier.width(20.dp))
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        val job =
                                                            notificationStudentViewModel.deleteNotification(
                                                                n
                                                            )
                                                        job.join()
                                                        navController.navigate("notificationStudentPrincipal")
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
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Cancelar",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            }
                            }
                        }
                    }
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == notificationState?.lastIndex }
                            .collect { isAtBottom ->
                                if (isAtBottom) {
                                    notificationStudentViewModel.loadMoreNotifications()
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
                            painter = painterResource(id = R.drawable.calendario),
                            contentDescription = "calendario",
                            modifier = Modifier
                                .size(52.dp)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                    Box(modifier = Modifier.weight(0.1f))
                        IconButton(onClick = { navController.navigate("HomeStudentScreen") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_home),
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
                        IconButton(onClick = { navController.navigate("notificationStudentPrincipal") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.notifications),
                                contentDescription = "calendario",
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(4.dp),
                                tint = Color(0xFF3F21DB)
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