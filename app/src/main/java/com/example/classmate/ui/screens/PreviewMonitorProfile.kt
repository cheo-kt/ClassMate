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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.classmate.domain.model.MonitorSubject
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.FormatterDate
import com.example.classmate.ui.viewModel.PreviewMonitorViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PreviewMonitorProfile(
    navController: NavController,
    monitor: String?,
    student: String?,
    materia: String?,

    previewMonitorViewModel: PreviewMonitorViewModel  = viewModel()

) {

    val monitorState by previewMonitorViewModel.monitorState.observeAsState();
    val opinionlist by previewMonitorViewModel.opinionList.observeAsState()
    val studentObj: Student = Gson().fromJson(student, Student::class.java)
    val monitorObj: Monitor = Gson().fromJson(monitor, Monitor::class.java)
    val subjectObj: MonitorSubject = Gson().fromJson(materia, MonitorSubject::class.java)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val image by previewMonitorViewModel.image.observeAsState()
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()

    LaunchedEffect(true) {
        previewMonitorViewModel.getOpinionMonitor(monitorObj.id)
        previewMonitorViewModel.getMonitorPhoto(monitorObj.photoUrl)
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
                            .offset(y = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            modifier = Modifier.fillMaxSize(1f),
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
                                .height(500.dp)
                                .fillMaxWidth(1f)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(170.dp) // Tamaño de la Box (que será un círculo)
                        .clip(CircleShape)
                        .background(Color(0xFFCCD0CF))
                ){

                    Image(
                        modifier = Modifier
                            .size(170.dp) // Tamaño de la imagen
                            .clip(CircleShape) // Hace que la imagen sea circular
                            .size(200.dp)
                            .fillMaxSize()
                        ,
                        contentDescription = null,
                        painter = rememberAsyncImagePainter(image, error = painterResource(R.drawable.botonestudiante)),
                        contentScale = ContentScale.Crop
                    )
                }

                monitorObj.let {
                    androidx.compose.material3.Text(
                        text = it.name + " " + it.lastname, fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(70.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.kid_star),
                                contentDescription = "Rating",
                                tint = Color(0xFF3F51B5),
                                modifier = Modifier.size(60.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.estrella),
                                contentDescription = "Rating Overlay",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Text(
                            text = "${monitorObj.rating}/5★",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.attach_money),
                                contentDescription = "Price",
                                tint = Color(0xFF3F51B5),
                                modifier = Modifier.size(60.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.dinero),
                                contentDescription = "Price Overlay",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Text(
                            text = "${subjectObj.price}$",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    "unicastMonitoring?monitor=${Gson().toJson(monitorObj) ?: "No"}&student=${Gson().toJson(studentObj) ?: "No"}&materia=${Gson().toJson(subjectObj)}"
                                )
                            },
                            modifier = Modifier.size(70.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(70.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.calendar_today),
                                    contentDescription = "Calendar",
                                    tint = Color(0xFF3F51B5),
                                    modifier = Modifier.size(60.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.calendario),
                                    contentDescription = "Calendar Overlay",
                                    tint = Color.White,
                                    modifier = Modifier.size(35.dp)
                                )
                            }
                        }
                        Text(
                            text = "Agendar",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Acerca del monitor:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = monitorObj.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }

                Text(
                    text = "Opiniones:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                LazyColumn(state = listState) {
                        opinionlist?.let { op ->
                            op.forEachIndexed{ _, n ->
                                item {
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
                                        Icon(
                                            imageVector = Icons.Outlined.PlayArrow,
                                            contentDescription = "a",
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                        androidx.compose.material3.Text(
                                            text = n!!.opinion,
                                            fontSize = 12.sp,
                                            color = Color.Blue,
                                        )

                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .align(Alignment.CenterEnd)
                                                    .padding(horizontal = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                androidx.compose.material3.Text(
                                                    text = "${n.calification}/5★"
                                                )
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        }
                    }
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == opinionlist?.lastIndex }
                            .collect { isAtBottom ->
                                if (isAtBottom) {
                                    previewMonitorViewModel
                                        .getOpinionMonitor(monitorObj.id)
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
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFFCCD0CF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { navController.navigate("HomeStudentScreen") }) {
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
                    IconButton(onClick = { navController.navigate("notificationStudentPrincipal") }) {
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
                    IconButton(onClick = { navController.navigate("chatScreenStudent") }) {
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
    }
}

