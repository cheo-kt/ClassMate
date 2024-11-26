package com.example.classmate.ui.screens

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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Type_Notification
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.components.FormatterDate
import com.example.classmate.ui.components.FormatterHour
import com.example.classmate.ui.viewModel.NotificationMonitorViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun NotificationMonitorScreen(navController: NavController,
                              notificationMonitorViewModel: NotificationMonitorViewModel = viewModel()
) {

    var expanded by remember { mutableStateOf(false) }
    val monitor: Monitor? by notificationMonitorViewModel.monitor.observeAsState(initial = null)
    val monitorState by notificationMonitorViewModel.monitorState.observeAsState()
    val notificationState by notificationMonitorViewModel.notificationList.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val image:String? by notificationMonitorViewModel.image.observeAsState()
    val scope = rememberCoroutineScope()
    if(monitor?.photoUrl?.isNotEmpty() == true){
        monitor?.let { notificationMonitorViewModel.getMonitorImage(it.photoUrl) }
    }

    LaunchedEffect(true) {
        notificationMonitorViewModel.getMonitor()
    }

    LaunchedEffect(true) {
        val job = notificationMonitorViewModel.verifyAppointments()
        job.join()
        val job2 = notificationMonitorViewModel.loadRandomSuggestion(monitor!!)
        job2.join()
        notificationMonitorViewModel.loadMoreMonitorNotifications()
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
                    .height(105.dp)
                    .background(Color(0xFF209619)),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset((-5).dp)
                ) {

                    IconButton(
                        onClick = {
                            navController.navigate("")
                        },
                        modifier = Modifier
                            .size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            modifier = Modifier.size(45.dp),
                            tint = Color.White
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.classmatelogo),
                        contentDescription = "classMateLogo",
                        modifier = Modifier.size(150.dp)
                            .padding(horizontal = 10.dp)
                            .aspectRatio(1.1f),
                        contentScale = ContentScale.Crop
                    )

                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = {})
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize().offset(y=5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .width(45.dp)
                                .aspectRatio(1f)
                                .background(Color.Transparent)
                                .clickable(onClick = {navController.navigate("notificationMonitorScreen")})
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
                                .width(45.dp)
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
                            }, onDismiss = { expanded = false })
                        }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFCCD0CF))
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                                                    color = Color(0xFF026900),
                                                )
                                                androidx.compose.material3.Text(
                                                    text = n.studentName,
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
                                                        text = if (n!!.type == Type_Notification.RECORDATORIO) {
                                                            FormatterHour(timestamp = n.dateMonitoring)
                                                        } else {
                                                            FormatterDate(timestamp = n.aceptationDate)
                                                        },
                                                        fontSize = 16.sp,
                                                    )
                                                    Spacer(modifier = Modifier.width(20.dp))
                                                    IconButton(
                                                        onClick = {
                                                            scope.launch {
                                                                val job =
                                                                    notificationMonitorViewModel.deleteNotificationById(
                                                                        n.id
                                                                    )
                                                                job.join()
                                                                navController.navigate("notificationMonitorScreen")
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
                                    notificationMonitorViewModel.loadMoreMonitorNotifications()
                                }
                            }
                    }
                }
            }
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
                        IconButton(onClick = {navController.navigate("HomeMonitorScreen")}) {
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
}