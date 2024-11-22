package com.example.classmate.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.classmate.domain.model.Message
import com.example.classmate.ui.viewModel.AppoimentChatViewModel

@Composable
fun AppointmentChatScreen(
    navController: NavController,
    appointmentId: String,
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
        Column(modifier = Modifier.padding(innerPadding)) {
            appointmentState?.let { appointment ->
                Text(
                    text = "Chat sobre: ${appointment.subjectname} con ${appointment.monitorName}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messagesState) { message ->
                    message?.let {
                        ChatMessageCard(message, isMine = message.isMine)
                    }
                }
            }

            MessageComposer(appointmentId)
        }
    }
}

@Composable
fun ChatMessageCard(message: Message, isMine: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isMine) MaterialTheme.colorScheme.primaryContainer else Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            message.imageURL?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = message.content,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun MessageComposer(
    appointmentId: String,
    appointmentChatViewModel: AppoimentChatViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    var selectedUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedUri = uri
    }

    Column {
        selectedUri?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
        Row {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f)
            )

            Button(onClick = { launcher.launch("image/*") }) {
                Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "Adjuntar imagen")
            }

            Button(onClick = {
                appointmentChatViewModel.sendMessage(messageText, selectedUri, appointmentId)
                selectedUri = null
                messageText = ""
            }) {
                Text(text = "Enviar")
            }
        }
    }
}
