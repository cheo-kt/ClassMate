package com.example.classmate.ui.screens.tutorialStudent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classmate.R
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.Type_Notification
import com.example.classmate.ui.components.FormatterDate
import com.google.firebase.Timestamp

@Composable
fun guia7StudentScreen(navController: NavController) {
    val notificationState = listOf(
        Notification(
            id = "1",
            aceptationDate = Timestamp.now(),
            dateMonitoring = Timestamp.now(),
            content = "Nueva tarea disponible",
            subject = "Matemáticas",
            studentId = "s123",
            studentName = "Alicia Pérez",
            monitorId = "m001",
            monitorName = "Juan Gómez",
            Type_Notification.RECORDATORIO
        ),
        Notification(
            id = "2",
            aceptationDate = Timestamp.now(),
            dateMonitoring = Timestamp.now(),
            content = "Prueba próxima el viernes",
            subject = "Historia",
            studentId = "s124",
            studentName = "Roberto Martínez",
            monitorId = "m002",
            monitorName = "Emma Torres",
            Type_Notification.RECORDATORIO
        ),
        Notification(
            id = "3",
            aceptationDate = Timestamp.now(),
            dateMonitoring = Timestamp.now(),
            content = "Tarea para mañana",
            subject = "Ciencias",
            studentId = "s125",
            studentName = "Carmen Ramírez",
            monitorId = "m003",
            monitorName = "Liam Díaz",
            Type_Notification.RECORDATORIO
        ),
        Notification(
            id = "4",
            aceptationDate = Timestamp.now(),
            dateMonitoring = Timestamp.now(),
            content = "Presentación de proyecto la próxima semana",
            subject = "Inglés",
            studentId = "s126",
            studentName = "David López",
            monitorId = "m004",
            monitorName = "Olivia Hernández",
            Type_Notification.RECORDATORIO
        ),
        Notification(
            id = "5",
            aceptationDate = Timestamp.now(),
            dateMonitoring = Timestamp.now(),
            content = "Clase extra programada para el miércoles",
            subject = "Física",
            studentId = "s127",
            studentName = "Eva Sánchez",
            monitorId = "m005",
            monitorName = "José Martínez",
            Type_Notification.RECORDATORIO
        )
    )


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
                            onClick = {},
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(70.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                painter = painterResource(R.drawable.botonestudiante),
                                contentDescription = "foto de perfil",
                                contentScale = ContentScale.Crop
                            )
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
                    .weight(1f),
            ) {
                Column(
                ) {
                    notificationState?.let { notification ->
                            notification.forEachIndexed{ _, n ->
                                ElevatedCard(
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 5.dp,
                                    ), modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                        .clickable {
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
                                                    text = FormatterDate(timestamp = n!!.aceptationDate),
                                                    fontSize = 16.sp,
                                                )
                                                Spacer(modifier = Modifier.width(20.dp))
                                                IconButton(
                                                    onClick = {
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
                    IconButton(onClick = {  }) {
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
                    IconButton(onClick = {  }) {
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
                        IconButton(onClick = {  }) {
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
                        text = "Aqui podras observar tus notificaciones.",
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
                            navController.navigate("HomeStudentScreen")
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ){
                    androidx.compose.material.Text(
                        text = "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                }
            }
            Box(modifier = Modifier.weight(0.04f))
        }

    }
}
