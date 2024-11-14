package com.example.classmate.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.classmate.domain.model.Notification
import com.example.classmate.domain.model.OpinionsAndQualifications
import com.example.classmate.ui.components.RatingBar
import com.example.classmate.ui.viewModel.OpinionStudentViewModel
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.launch

@SuppressLint("Range")
@Composable
fun OpinionStudentScreen(navController: NavController, notification: String?, opinionStudentViewModel: OpinionStudentViewModel = viewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    var note by remember { mutableStateOf("") }
    var notiObj: Notification = Gson().fromJson(notification, Notification::class.java)
    var rating by remember { mutableStateOf(3) }
    val scope = rememberCoroutineScope()
    val maxLength = 30

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(Color(0xFF3F21DB)),
                contentAlignment = Alignment.Center
            ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopStart)
                            .offset(y = 40.dp)
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
                                .height(400.dp)
                                .fillMaxWidth(1f)
                        )
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
                        text = notiObj.monitorName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(12.dp)
                    )
                }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "¿Que tal de parecio?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
            RatingBar(
                rating = rating,
                onRatingChanged = { newRating -> rating = newRating })
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "¡Cuéntanos al respecto!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Box(modifier = Modifier.weight(0.01f))

            Box(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(0.1f)) {

                OutlinedTextField(
                    value = note,
                    onValueChange = { if (it.length <= maxLength) {
                        note = it
                    }else{
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Limite alcanzado")
                        }
                    }},
                    label = { androidx.compose.material3.Text("Escribe tu Opinion (Máximo 30 letras)") },
                    modifier = Modifier
                        .fillMaxSize() // Asegura que ocupe todo el ancho disponible
                        .padding(horizontal = 16.dp) // Padding horizontal
                        .height(150.dp), // Ajusta la altura del TextField
                    readOnly = false // Permite que el usuario edite el campo
                )
            }
            Box(modifier = Modifier.weight(0.01f))

            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(
                    onClick = {
                        opinionStudentViewModel.calificateMonitor(
                            OpinionsAndQualifications("",rating,note
                            ),
                            notiObj.monitorId
                        )
                            scope.launch {
                                val job = opinionStudentViewModel.deleteNotification(notiObj)
                                job.join()
                                navController.navigate("notificationStudentPrincipal")
                            }
                              },

                    modifier = Modifier
                        .wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F21DB)),

                    ) {
                    Text(
                        text = "Guardar y publicar",
                        color = Color.White
                    )
                }
            }
            Box(modifier = Modifier.weight(0.01f))

        }
    }
}