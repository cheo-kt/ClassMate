package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.HomeStudentViewModel
import kotlinx.coroutines.launch


@Composable
fun HomeStudentScreen(navController: NavController, homeStudentViewModel: HomeStudentViewModel = viewModel()) {
    val monitorState by homeStudentViewModel.monitorList.observeAsState()
    val scrollState = rememberScrollState()
    val student: Student? by homeStudentViewModel.student.observeAsState(initial = null)
    var image = student?.photo
    val studentState by homeStudentViewModel.studentState.observeAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var expandedFilter by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        homeStudentViewModel.getMonitors()
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
                    .height(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.encabezadoestudaintes),
                    contentDescription = "Encabezado",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

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
                            .clickable(onClick = { /*TODO*/ })
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
                            }, onDismiss = { expanded = false })

                            DropdownMenuItemWithSeparator("Cerrar sesión", onClick = {
                            }, onDismiss = { expanded = false })
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
            Box(modifier = Modifier.weight(0.001f))
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "¿Qué necesitas?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                        // Añade padding para más espacio vertical

                    ) {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            ) ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),

                        ){

                            Icon(
                                painter = painterResource(id = R.drawable.data_loss_prevention),
                                contentDescription = "Search Icon",
                                tint = Color.Black,
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp)

                            )
                            Text(text = "Filtrar por: ")

                        }

                        DropdownMenu(
                            expanded = expandedFilter,
                            onDismissRequest = { expandedFilter = false },
                            modifier = Modifier
                                .background(Color(0xFFCCD0CF))
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                        ) {

                        }

                    }

                    Text(
                        text = "Monitores Destacados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        monitorState?.let { monitors ->
                            monitors.forEach { monitor ->
                                monitor?.subjects?.forEach { subject ->
                                    ElevatedCard(
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 20.dp,
                                        ), modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        onClick = {}
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
                                                    text = ("Materia: ${subject.nombre}"),
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
                                                        text = "$" + subject.precio,
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
                                                        .padding(end = 10.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.DateRange,
                                                        contentDescription = "Calendar",
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .padding(end = 5.dp)
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Outlined.PlayArrow,
                                                        contentDescription = "Arrow",
                                                        modifier = Modifier.size(40.dp)
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
                        IconButton(onClick = { /*TODO*/ }) {
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
                        IconButton(onClick = { /*TODO*/ }) {
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
        }

}

