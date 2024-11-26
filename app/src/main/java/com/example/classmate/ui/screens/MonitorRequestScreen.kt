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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.MonitorRequestViewModel
import com.google.gson.Gson

@Composable
fun MonitorRequestScreen(navController: NavController, monitorRequestViewModel: MonitorRequestViewModel = viewModel()) {
    val requestState by monitorRequestViewModel.list.observeAsState()
    val scrollState = rememberScrollState()
    var filter by remember { mutableStateOf("") }
    val monitor by monitorRequestViewModel.monitor.observeAsState()
    var image = monitor?.photoUrl
    var expanded by remember { mutableStateOf(false) }
    val maxLength = 20
    val listState = rememberLazyListState()
    LaunchedEffect(true) {
        monitorRequestViewModel.getMonitor()
        monitorRequestViewModel.loadMoreRequest()
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
                                .clickable(onClick = {})
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
                                .clickable(onClick = {})
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
                        text = "¡Estas son tus solicitudes de monitoria!",
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
                                        .background(Color.LightGray, shape = RoundedCornerShape(50))
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
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        requestState?.let { requests ->
                            val rb:List<Request?> = if(filter.isNotEmpty()) {
                                requests.filter {
                                    it?.studentName!!.startsWith(
                                        filter,
                                        ignoreCase = true
                                    )
                                }
                            } else {
                                requests
                            }
                            rb.forEach { request ->
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
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .padding(20.dp)
                                        ) {
                                            androidx.compose.material3.Text(
                                                text = request!!.studentName,
                                                color = Color(0xFF209619),
                                                fontSize = 16.sp,
                                            )
                                            androidx.compose.material3.Text(
                                                text = ("Materia:" + request.subjectname),
                                                fontSize = 12.sp,
                                            )
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
                                                        "DecisionMonitorUnicast?request=${
                                                            Gson().toJson(
                                                                request
                                                            ) ?: "No"
                                                        }&monitor=${
                                                            Gson().toJson(
                                                                monitor
                                                            ) ?: "No"
                                                        }"
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.PlayArrow,
                                                        contentDescription = "Arrow",
                                                        modifier = Modifier.size(50.dp)
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
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == requestState?.lastIndex }
                            .collect { isAtBottom ->
                                if (isAtBottom) {
                                    monitorRequestViewModel.loadMoreRequest()
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
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFF026900), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ){
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

                    }

                    Box(modifier = Modifier.weight(0.1f))

                    IconButton(onClick = {navController.navigate("HomeMonitorScreen")  }) {
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
}