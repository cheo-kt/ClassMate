package com.example.classmate.ui.screens

import android.annotation.SuppressLint
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.content.MediaType.Companion.Text
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalConfiguration
import com.example.classmate.ui.viewModel.OpinionStudentViewModel

@SuppressLint("Range")
@Composable
fun OpinionStudentScreen(navController: NavController, opinionStudentViewModel: OpinionStudentViewModel = viewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }
    var note by remember { mutableStateOf("") }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.encabezadoestudaintes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopStart)
                            .offset(y = 20.dp)
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
                                .height(500.dp).fillMaxWidth(1f)
                        )
                    }
                }
            }
            Box(Modifier.fillMaxWidth()
                .background(Color(0xFFCCD0CF))
            ){
                Column(Modifier.fillMaxWidth().align(Alignment.CenterEnd)) {
                    Text(
                        text = "EJEMPLO DE MONITOR",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(12.dp)
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                }

            }
            Box(modifier = Modifier.weight(0.01f))
            Text(
                text = "¿Que tal de parecio?",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Box(modifier = Modifier.weight(0.01f))
            Box(Modifier.fillMaxWidth()){
                Row(modifier = Modifier.padding(horizontal =2.dp).fillMaxWidth()) {
                    val starSize = LocalConfiguration.current.screenWidthDp.dp / 8
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color.Black,
                        modifier = Modifier.size(starSize)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(starSize)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(starSize)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(starSize)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(starSize)
                            .border(width = 3.dp, color = Color.Black)
                    )
                }
            }
            Box(modifier = Modifier.weight(0.01f))
            Text(
                text = "¡Cuéntanos al respecto!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Box(modifier = Modifier.weight(0.01f))

            Box(modifier = Modifier.fillMaxHeight()
                .fillMaxWidth()
                .weight(0.1f)) {

                OutlinedTextField(
                    value = note,
                    onValueChange = { newNote -> note = newNote },
                    label = { androidx.compose.material3.Text("Escribe tu Opinion") },
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
                    onClick = {},
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