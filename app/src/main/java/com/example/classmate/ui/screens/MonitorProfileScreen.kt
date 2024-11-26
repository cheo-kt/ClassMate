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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.ui.viewModel.MonitorProfileViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun MonitorProfileScreen(navController: NavController, authViewModel: MonitorProfileViewModel = viewModel()){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val monitorState by  authViewModel.monitorState.observeAsState()
    val monitor: Monitor? by authViewModel.monitor.observeAsState(initial = null)
    val image by authViewModel.image.observeAsState()
    val scrollState= rememberScrollState()
    val snackbarHostState= remember { SnackbarHostState()}
    val scope= rememberCoroutineScope()
    if (monitor?.photoUrl?.isNotEmpty() == true) {
        monitor?.let { authViewModel.getMonitorImage(it.photoUrl) }
    }
    LaunchedEffect(true) {
        authViewModel.showMonitorInformation()
        if(monitor?.photoUrl?.isEmpty()== false){
            monitor?.let { authViewModel.getMonitorImage(it.photoUrl) }
        }
    }
    LaunchedEffect (navBackStackEntry){
        authViewModel.showMonitorInformation()
        if(monitor?.photoUrl?.isEmpty()== false){
            monitor?.let { authViewModel.getMonitorImage(it.photoUrl) }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF209619))
                    .height(120.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("HomeMonitorScreen")
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Icon(

                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        modifier = Modifier.size(50.dp),
                        tint= Color.White

                    )
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
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(200.dp) // Tamaño de la Box (que será un círculo)
                    .clip(CircleShape)
                    .background(Color(0xFFCCD0CF))
            )
            {
                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize()
                    ,contentScale = ContentScale.Crop,
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(image
                        , error = painterResource(R.drawable.botonestudiante))

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
                    .width(300.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "  Acerca de mi: ", modifier = Modifier.offset(0.dp,3.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    monitor?.let {
                        Text(text = "  "+it.description)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    monitor?.let {
                        Text(text = "Telefono: "+it.phone)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Email: "+it.email)
                    } ?: run {
                        Text(text = "NO_PHONE")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "NO_EMAIL")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if(!image.isNullOrEmpty()){
                    navController.navigate("monitorEdit?monitor=${Gson().toJson(monitor)?:"No"}&image=${URLEncoder.encode(image, "UTF-8")}")
                }else{
                    val noImage = "noImage"
                    navController.navigate("monitorEdit?monitor=${Gson().toJson(monitor)?:"No"}&image=${noImage}")
                }
            }) {
                Text(text = "Editar perfil")
            }
        }
    }

}

