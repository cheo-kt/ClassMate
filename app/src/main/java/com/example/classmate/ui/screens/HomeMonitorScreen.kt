package com.example.classmate.ui.screens

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
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.RequestBroadcast
import com.example.classmate.domain.model.Student
import com.example.classmate.domain.model.Subject
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.components.RequestBroadcastCard
import com.example.classmate.ui.viewModel.HomeMonitorViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.sqrt

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
    if(monitor?.photoUrl?.isNotEmpty() == true){
        monitor?.let { homeMonitorViewModel.getMonitorPhoto(it.photoUrl) }
    }
    var expanded by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }
    var subjectFilteredList by remember { mutableStateOf(emptyList<Subject>()) }
    val maxLength = 20
    var subjectIdList by remember { mutableStateOf(emptyList<String>()) }
    var filteringType by remember { mutableStateOf("Materia") }
    var filterSubjectName by remember { mutableStateOf("") }
    var isSearch= false
    val listState = rememberLazyListState()
    val subjectsState by homeMonitorViewModel.subjectList.observeAsState()
    var buttonMessage by remember { mutableStateOf("Materia no seleccionada") }

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
                                .clickable(onClick = {navController.navigate("helpMonitor")})
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
                                            subjectIdList = emptyList()
                                        }, onDismiss = { expandedFilter = false })
                                        DropdownMenuItemWithSeparator("Tipo", onClick = {
                                            filteringType = "Tipo"
                                            expandedFilter = false
                                            subjectIdList = emptyList()
                                        }, onDismiss = { expandedFilter = false })
                                    }

                                }
                                IconButton(onClick = {
                                    isSearch = true
                                    if (filteringType == "Fecha") {
                                        // homeMonitorViewModel.monitorsFilteredByName(filter)
                                    } else if (filteringType == "Materia") {
                                          //subjectIdList es una lista de ids de materias??buttonMessage
                                        if( buttonMessage != "Materia no seleccionada") {

                                             homeMonitorViewModel.monitorsFilteredBySubject(buttonMessage)
                                            Log.e("NombreMateria", "El nombre de la materia es : $buttonMessage" )


                                        }

                                    } else if (filteringType == "Tipo") {
                                        TODO()
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

                                    buttonMessage = "Materia no seleccionada"
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
                    Spacer(modifier = Modifier.height(2.dp))
                    androidx.compose.material3.Text(
                        text = "Solicitudes Broadcast",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        if(filterrequestState!!.isNotEmpty() && filteringType == "Materia"){
                            filterrequestState?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController =navController
                                )
                            }

                        }else {
                            requestState?.let { requests ->
                                RequestBroadcastCard(
                                    monitor = monitor,
                                    requests = requests,
                                    filter = filter,
                                    navController =navController
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
