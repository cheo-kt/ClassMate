package com.example.classmate.ui.screens


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Appointment
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Request
import com.example.classmate.domain.model.Type_Notification
import com.example.classmate.ui.components.DropdownMenuItemWithSeparator
import com.example.classmate.ui.viewModel.AppointmentViewModel
import com.example.classmate.ui.viewModel.RequestBroadcastStudentViewModel
import com.example.classmate.ui.viewModel.UnicastMonitoringViewModel
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun guia7MonitorScreen(
    navController: NavController,
    request: String?,
    monitor: String?,
) {
    val scope = rememberCoroutineScope()
    val requestObj: Request = Gson().fromJson(request, Request::class.java)
    val monitorObj: Monitor = Gson().fromJson(monitor, Monitor::class.java)
    var image = monitor
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(scrollState),
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
                                navController.navigate("MonitorRequest")
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
                                .clickable(onClick = {})
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
                            onClick = { },
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
                            }
                        }
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start // Alineación a la izquierda
            ) {
                // Header section
                Text(
                    text = requestObj.studentName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = requestObj.subjectname,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tipo de ayuda: ${requestObj.type}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                // Schedule section

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Text(
                        text = "Horarios:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Column {
                        val spanishLocale = Locale("es", "ES")
                        val dateFormatter = SimpleDateFormat(
                            "EEEE dd 'de' MMMM 'de' yyyy 'a las' HH:mm",
                            spanishLocale
                        )

                        Text(
                            text = dateFormatter.format(requestObj.dateInitial.toDate()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = dateFormatter.format(requestObj.dateFinal.toDate()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Column {
                        Text(
                            text = "Presencial/Virtual:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        if (requestObj.mode_class == "Virtual") {
                            Text(requestObj.mode_class)

                        } else {
                            Text("${requestObj.mode_class}, en : ${requestObj.mode_class}")

                        }

                    }

                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start // Alineación a la izquierda
                ) {
                    Text(
                        text = "Descripción:",
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        Text(
                            text = requestObj.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }


            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Buttons section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(width = 160.dp, height = 48.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Aceptar",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    onClick = {
                    },
                    modifier = Modifier
                        .size(width = 160.dp, height = 48.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Rechazar",
                        modifier = Modifier.size(24.dp)
                    )
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
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(color = Color(0xFF026900), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { }) {
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

                    IconButton(onClick = { }) {
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
                    IconButton(onClick = {  }) {
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
                    IconButton(onClick = {  }) {
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
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)) // Fondo gris transparente
        )

        Column(modifier = Modifier.align(Alignment.Center)){
            Box(modifier = Modifier.weight(0.1f))
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape) // Borde opcional
                    .background(Color.Transparent)
                    .align(Alignment.CenterHorizontally)
            )
            Column {

                Box(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    androidx.compose.material.Text(
                        text = "Aqui puedes ver los detalles de la solicitud seleccionada y puedes rechazar o aceptar dicha solicitud.",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Botón "Continuar"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            // Acción de navegación al presionar "Continuar"
                            navController.navigate("guia8Monitor")
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ){
                    androidx.compose.material.Text(
                        text = "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
            }
            Box(modifier = Modifier.weight(0.04f))
        }

    }

}
