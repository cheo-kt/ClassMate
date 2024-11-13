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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.classmate.domain.model.Monitor
import com.example.classmate.domain.model.MonitorSubject

@Composable
fun guia2StudentScreen(navController: NavController) {
    val monitorState =listOf(
        Monitor(
            id = "1",
            name = "Juan",
            lastname = "Pérez",
            phone = "3001234567",
            subjects = listOf(
                MonitorSubject(subjectId = "1", name = "Matemáticas", price = "20/h"),
                MonitorSubject(subjectId = "2", name = "Física", price = "25/h")
            ),
            email = "juan.perez@example.com",
            description = "Monitor con experiencia en ciencias exactas.",
            photoUrl = "",
            rating = 4.5
        ),
        Monitor(
            id = "2",
            name = "Ana",
            lastname = "Gómez",
            phone = "3002345678",
            subjects = listOf(
                MonitorSubject(subjectId = "3", name = "Historia", price = "15/h"),
                MonitorSubject(subjectId = "4", name = "Geografía", price = "18/h")
            ),
            email = "ana.gomez@example.com",
            description = "Especialista en ciencias sociales.",
            photoUrl = "",
            rating = 4.2
        ),
        Monitor(
            id = "3",
            name = "Carlos",
            lastname = "Martínez",
            phone = "3003456789",
            subjects = listOf(
                MonitorSubject(subjectId = "5", name = "Programación", price = "30/h"),
                MonitorSubject(subjectId = "6", name = "Bases de Datos", price = "28/h")
            ),
            email = "carlos.martinez@example.com",
            description = "Desarrollador de software con amplia experiencia en programación.",
            photoUrl = "",
            rating = 4.7
        ),
        Monitor(
            id = "4",
            name = "Laura",
            lastname = "Rodríguez",
            phone = "3004567890",
            subjects = listOf(
                MonitorSubject(subjectId = "7", name = "Inglés", price = "22/h"),
                MonitorSubject(subjectId = "8", name = "Literatura", price = "20/h")
            ),
            email = "laura.rodriguez@example.com",
            description = "Profesora de idiomas con enfoque en literatura y escritura.",
            photoUrl = "",
            rating = 4.3
        )
    )

    var filter by remember { mutableStateOf("") }

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
                        painter = painterResource(id = R.drawable.encabezadoestudaintes),
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
                                .clickable(onClick = { /* TODO: Acción de ayuda */ })
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.live_help),
                                contentDescription = "Ayuda",
                                tint = Color.White,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        // Botón de foto de perfil
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                painter =  painterResource(R.drawable.botonestudiante),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "¿Qué necesitas?",
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
                        Column {
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
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                } else {
                                    Text(
                                        text = filter,
                                        color = Color.Black,
                                        style = TextStyle(fontSize = 16.sp)
                                    )
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.data_loss_prevention),
                                    contentDescription = "Search Icon",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterEnd)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 15.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = "Filtrar por materia",
                                    color = Color.Gray,
                                    style = TextStyle(fontSize = 16.sp)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.data_loss_prevention),
                                    contentDescription = "Search Icon",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterEnd)
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Monitores Destacados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Column() {

                        // Asegúrate de que 'monitorState' sea una lista no vacía y esté bien definida.
                        val filteredMonitors = if (filter.isNotEmpty()) {
                            monitorState.filter {
                                it.name.startsWith(filter, ignoreCase = true)
                            }
                        } else {
                            monitorState
                        }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredMonitors) { monitor ->
                                monitor.subjects.forEach { subject ->
                                    CreateMonitorCard(
                                        monitor = monitor,
                                        subject = subject,
                                    )
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
                    IconButton(onClick = { }) {
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
                    IconButton(onClick = {}) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)) // Fondo gris transparente
        )

        Column(modifier = Modifier.align(Alignment.Center)){
            Box(modifier = Modifier.weight(0.04f))
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
                    Text(
                        text = "Aqui puedes seleccionar filtros para ver monitores de materias en especificos.",
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
                            navController.navigate("")
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                }
            }
            Box(modifier = Modifier.weight(0.1f))
        }

    }
}


