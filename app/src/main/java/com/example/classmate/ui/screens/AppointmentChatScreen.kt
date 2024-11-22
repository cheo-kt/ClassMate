package com.example.classmate.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classmate.R
import com.example.classmate.domain.model.Message
import com.example.classmate.ui.viewModel.AppoimentChatViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.typeOf

@Composable
fun AppointmentChatScreen(
    navController: NavController,
    appointmentId: String,
    type: Boolean,
    appointmentChatViewModel: AppoimentChatViewModel = viewModel()
) {
    val messagesState by appointmentChatViewModel.messagesState.observeAsState(emptyList())
    val appointmentState by appointmentChatViewModel.appointmentState.observeAsState(null)
    val lazyColumnState = rememberLazyListState()
    var imagesLoaded by remember { mutableStateOf(0) }


    LaunchedEffect(appointmentId) {
        appointmentChatViewModel.getAppointment(appointmentId)
        appointmentChatViewModel.getMessagesLiveMode(appointmentId)
    }

    LaunchedEffect(messagesState, imagesLoaded) {
        if (messagesState.isNotEmpty()) {
            val itemCount = messagesState.lastIndex
            lazyColumnState.animateScrollToItem(itemCount)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    if (type) {
                        Image(
                            painter = painterResource(id = R.drawable.encabezadoestudaintes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.encabezado),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart) // Aseguramos que la fila de icono y texto esté en la parte superior
                            .padding(16.dp) // Ajuste de padding según necesites
                    ) {
                        // Icono de regreso
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterVertically) // Alineamos verticalmente al centro
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(50.dp),
                                tint = Color.White
                            )
                        }

                        // Textos al lado del icono
                        appointmentState?.let { appointment ->
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically) // Alineamos los textos al centro verticalmente
                                    .padding(start = 16.dp) // Espaciado entre el icono y los textos
                            ) {
                                if (type) {
                                    Text(
                                        text = appointment.monitorName,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White // Puedes ajustar el color aquí
                                    )
                                } else {
                                    Text(
                                        text = appointment.studentName,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White // Puedes ajustar el color aquí
                                    )
                                }

                                Text(
                                    text = appointment.subjectname,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White // Puedes ajustar el color aquí
                                )
                            }
                        }
                    }
                }
            }



            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messagesState) { message ->
                    message?.let {
                        ChatMessageCard(message, isMine = message.isMine,type)
                    }
                }
            }

            MessageComposer(appointmentId,type)
        }
    }
}

@Composable
fun ChatMessageCard(message: Message, isMine: Boolean, type:Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if(type){if (isMine) Color(0xFF3F21DB) else Color(0xFF000057)}else{if (isMine) Color(0xFF209619) else Color(0xFF026900)},
            ),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                message.imageURL?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .heightIn(max = 200.dp)
                    )
                }
                Text(
                    text = message.content,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = Color.White
                )
                Row {
                    Text(
                        text = formatTimeFromTimestamp(message.date),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        color = Color.White
                    )
                }
            }
        }
    }
}

fun formatTimeFromTimestamp(timestamp: Timestamp): String {
    return try {
        val date = timestamp?.toDate()
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        date?.let { formatter.format(it) } ?: "Hora Inválida"
    } catch (e: Exception) {
        "Hora Inválida"
    }
}


@Composable
fun MessageComposer(
    appointmentId: String,
    type: Boolean,
    appointmentChatViewModel: AppoimentChatViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    var selectedUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedUri = uri
    }

    Column(Modifier.padding(vertical = 8.dp)) {
        selectedUri?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Row() {
            Box(modifier = Modifier.weight(0.01f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(if(type)Color(0xFF2E17BC)else Color(0xFF209619), shape = RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White, shape = RoundedCornerShape(12.dp)) // Fondo blanco con bordes redondeados
                            .border(2.dp, if(type) Color(0xFF000057) else Color(0xFF026900) , shape = RoundedCornerShape(12.dp)), // Bordes redondeados con color específico
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent, // El fondo se gestiona con el modificador
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre el TextField y los botones

                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircle,
                            contentDescription = "Adjuntar imagen",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre los botones

                    IconButton(
                        onClick = {
                            appointmentChatViewModel.sendMessage(messageText, selectedUri, appointmentId)
                            selectedUri = null
                            messageText = ""
                        },
                        modifier = Modifier
                            .size(48.dp) // Tamaño cuadrado para el botón
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.send), // Ícono de drawable
                            contentDescription = "Enviar mensaje",
                            tint = Color.White
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(0.01f))
        }

    }
}
