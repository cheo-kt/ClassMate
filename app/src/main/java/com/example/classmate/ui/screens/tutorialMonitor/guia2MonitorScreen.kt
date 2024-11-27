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
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@Composable
fun guia2MonitorScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var filter by remember { mutableStateOf("") }
    val maxLength = 20
    var filteringType by remember { mutableStateOf("Materia") }

    val requestState = listOf(
        RequestBroadcast(
            id = "1",
            mode_class = "Virtual",
            type = "Asesoría",
            dateInitial = Timestamp.now(),
            dateFinal = Timestamp.now(),
            description = "Ayuda para entender álgebra lineal",
            place = "Plataforma Zoom",
            subjectID = "MAT101",
            subjectname = "Álgebra Lineal",
            studentId = "S001",
            studentName = "Juan Pérez",
            notificationGenerated = true
        ),
        RequestBroadcast(
            id = "2",
            mode_class = "Presencial",
            type = "Taller",
            dateInitial = Timestamp.now(),
            dateFinal = Timestamp.now(),
            description = "Preparación para el examen final de cálculo",
            place = "Sala 205, Edificio A",
            subjectID = "MAT202",
            subjectname = "Cálculo Diferencial",
            studentId = "S002",
            studentName = "María Gómez",
            notificationGenerated = false
        ),
        RequestBroadcast(
            id = "3",
            mode_class = "Virtual",
            type = "Consulta",
            dateInitial = Timestamp.now(),
            dateFinal = Timestamp.now(),
            description = "Resolver dudas sobre programación en Kotlin",
            place = "Google Meet",
            subjectID = "PROG301",
            subjectname = "Programación Avanzada",
            studentId = "S003",
            studentName = "Carlos López",
            notificationGenerated = true
        )
    )
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
                                .clickable(onClick = { })
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
                            onClick = {  },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                painter = painterResource(R.drawable.botonestudiante),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop
                            )
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
                                Box(
                                    modifier = Modifier
                                        .weight(0.1f)
                                        .height(56.dp)
                                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 15.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = "Filtrar",
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
                                Box(Modifier.weight(0.01f))
                                Box(Modifier.weight(0.1f)) {

                                    Button(
                                        onClick = { },
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

                                }
                                IconButton(onClick = {
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
                    Spacer(modifier = Modifier.height(2.dp))
                    androidx.compose.material3.Text(
                        text = "Solicitudes Broadcast",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        requestState?.let { requests ->
                            RequestBroadcastCardIntroduction(
                                monitor = null,
                                requests = requests,
                                filter = filter,
                                navController =navController
                            )
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
                    IconButton(onClick = {  }) {
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
                    IconButton(onClick = { }) {
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
                        text = "Aqui puedes seleccionar filtros para ver monitorias con un tipo o materia especificos.",
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
                            navController.navigate("guia3Monitor")
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )
                }
            }
            Box(modifier = Modifier.weight(0.1f))
        }

    }
}



       