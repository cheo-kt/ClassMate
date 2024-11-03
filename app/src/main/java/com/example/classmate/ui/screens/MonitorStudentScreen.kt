package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.example.classmate.R

@Composable
fun MonitorStudentScreen(navController: NavController = rememberNavController()) { //Permito navegabilidad desde esta página
    Scaffold(modifier = Modifier.fillMaxSize()) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.encabezadoguiaregistro),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                            onClick = {
                                navController.navigate("signing")
                            },
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = (-25).dp, x = (-15).dp)
                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
                    Text(
                        text = "Registrarte como",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .offset(y = (-25).dp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .offset(y = (-25).dp) // Centra el Row horizontal y verticalmente
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.botonestudiante),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("signup")
                                }
                                .size(175.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Estudiante",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 35.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .offset(y = (-25).dp) // Centra el Row horizontal y verticalmente
                    ) {
                        Text(
                            text = "Monitor",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 35.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Image(
                            painter = painterResource(id = R.drawable.botontutor),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("signupMonitor")
                                }
                                .size(175.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}