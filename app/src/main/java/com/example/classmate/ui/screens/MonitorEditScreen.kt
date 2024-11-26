package com.example.classmate.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Monitor
import com.example.classmate.ui.viewModel.MonitorEditProfileViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun MonitorEditScreen(navController: NavController,monitor:String?,image:String?,authViewModel: MonitorEditProfileViewModel = viewModel()) {
    authViewModel.showMonitorInformation()
    val scrollState = rememberScrollState()
    val monitorObj:Monitor = Gson().fromJson(monitor, Monitor::class.java)
    var oldImage = image
    val monitorPhotoState by authViewModel.monitorPhotoState.observeAsState()
    var newImage by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isInitialized by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(monitorPhotoState) {
        if (monitorPhotoState == 3) {
            navController.navigate("monitorProfile")
        }
    }
    LaunchedEffect(!isInitialized) {
        name = monitorObj.name
        lastname = monitorObj.lastname
        phone = monitorObj.phone
        description = monitorObj.description
        email = monitorObj.email
        isInitialized = true

    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            newImage = it
            oldImage = it.toString()
        } ?: run {
            scope.launch {
                snackbarHostState.showSnackbar("No se seleccionó ninguna imagen")
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF209619))
                    .height(120.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("monitorProfile")
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
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
            )
            {

                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize(),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(
                        oldImage,
                        error = painterResource(R.drawable.botonestudiante)
                    ),
                    contentScale = ContentScale.Crop
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = name,
                onValueChange = { newName -> name = newName },
                label = { Text("Nombre") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = lastname,
                onValueChange = { newLastName -> lastname = newLastName },
                label = { Text("Apellido") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = phone,
                onValueChange = { newName -> phone = newName },
                label = { Text("Telefono") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = description,
                onValueChange = { newName -> description = newName },
                label = { Text("Descripcion") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = email,
                onValueChange = { newName -> email = newName },
                label = { Text("email") }
            )
            Button(onClick = {

                if (name == "" || description == ""
                    || lastname == "" || phone == ""
                ) {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Ninguno de los campos puede estar vacio")
                    }
                } else if (!emailRegex.matches(email)) {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Correo electronico mal escrito")

                    }
                } else {
                    if (name != monitorObj.name) {
                        authViewModel.updateMonitorProfile(monitorObj.id, "name", name)
                    }
                    if (lastname != monitorObj.lastname) {
                        authViewModel.updateMonitorProfile(monitorObj.id, "lastname", lastname)
                    }
                    if (phone != monitorObj.phone) {
                        authViewModel.updateMonitorProfile(monitorObj.id, "phone", phone)
                    }
                    if (description != monitorObj.description) {
                        authViewModel.updateMonitorProfile(
                            monitorObj.id,
                            "description",
                            description
                        )
                    }
                    if (email != monitorObj.email) {
                        authViewModel.updateMonitorProfile(monitorObj.id, "email", email)
                    }

                    if (newImage.toString().isNotBlank()) {

                        oldImage?.let { 
                            authViewModel.updateMonitorPhoto(newImage, context, it) 
                        }
                        if (monitorPhotoState == 2) {
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Error durante la subida de la imágen")
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("No se eligió ninguna imágen")
                        }
                    }
                }


            }) {
                Text(text = "Guardar Cambios")

            }

            Button(onClick = {

                imagePickerLauncher.launch("image/*")
            }) {
                Text(text = "Subir Foto")
            }

        }
        if (monitorPhotoState == 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        }
    }
}


