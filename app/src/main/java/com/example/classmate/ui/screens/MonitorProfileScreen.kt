package com.example.classmate.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.ui.viewModel.MonitorProfileViewModel

@Composable
fun MonitorProfileScreen(navController: NavController, authViewModel: MonitorProfileViewModel = viewModel()){
    authViewModel.showMonitorInformation()
    val monitor: Monitor? by authViewModel.monitor.observeAsState(initial = null)
    var image = ""
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3F21DB))
                    .height(120.dp)
            ) {
                Button(modifier = Modifier
                    .align(Alignment.CenterStart),
                    onClick = {
                        /*TODO*/
                    }) {

                }
                Image(
                    contentDescription = null,
                    painter = painterResource(id = R.drawable.classmatelogo),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                        .align(Alignment.Center)

                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(200.dp) // Tamaño de la Box (que será un círculo)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
            {
                monitor?.let {
                    image = it.photo
                }
                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize(),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(image)

                )

            }
            Spacer(modifier = Modifier.height(30.dp))
            monitor?.let {
                Text(text = it.name + " " + it.lastname)
            } ?: run {
                Text(text = "NO_NAME")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .width(270.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                monitor?.let {
                    Text(text = it.phone)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = it.email)
                } ?: run {

                    Text(text = "NO_PHONE")
                    Text(text = "NO_EMAIL")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }

}
@Preview
@Composable
fun test(){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3F21DB))
                    .height(120.dp)

            ) {
                Button(modifier = Modifier
                    .align(Alignment.CenterStart),
                    onClick = {
                        /*TODO*/
                    }) {

                }
                Image(
                    contentDescription = null,
                    painter = painterResource(id = R.drawable.classmatelogo),
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                        .align(Alignment.Center)

                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(200.dp) // Tamaño de la Box (que será un círculo)
                    .clip(CircleShape)
                    .background(Color.Red)
            )
            {
                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize(),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK3HCVKZhUpgT6SjGTMJHAZpkvU3S2bVosxw&s")

                )

            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Nombre de Monitor")
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .width(270.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                Text(text = "Nombre de Monitor")


            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .width(270.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                Text(text = "Nombre de Monitor")

            }
        }
    }
}
