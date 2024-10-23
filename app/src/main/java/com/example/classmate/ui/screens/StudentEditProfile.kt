package com.example.classmate.ui.screens

import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner.current
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.viewModel.StudentEditProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentEditScreen(navController: NavController,authViewModel: StudentEditProfileViewModel = viewModel()) {
    authViewModel.showStudentInformation()
    val student: Student? by authViewModel.student.observeAsState(initial = null)
    val scrollState = rememberScrollState()
    var oldImage = student?.photo
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
    LaunchedEffect(!isInitialized) {
        name = student?.name ?: ""
        lastname = student?.lastname ?:""
        phone = student?.phone ?:""
        description = student?.description ?:""
        email = student?.email ?:""
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

    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerpadding ->

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
                    .background(Color(0xFF3F21DB))
                    .height(120.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("studentProfile")
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
                student?.let {
                    oldImage = it.photo
                }
                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize()
                    ,
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(oldImage, error = painterResource(R.drawable.botonestudiante)),
                    contentScale = ContentScale.Crop
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = name,
                onValueChange = { newName -> name = newName },
                label = { Text("Nombre") },
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
                if(name == "" || description == ""
                    || lastname == "" || phone == "" ){
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Ninguno de los campos puede estar vacio")
                    }
                }else if (!emailRegex.matches(email)){
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Correo electronico mal escrito")
                    }
                }else{
                    authViewModel.updateStudentProfile(phone,name,lastname,description,email)

                    if (newImage.toString().isNotBlank()) {

                        authViewModel.updateStudentPhoto(newImage,context)
                        if (authViewModel.studentPhotoState.value==2){
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Error durante la subida de la imagen")

                            }
                        }
                    }else{
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("No se eligio ninguna imagen")

                        }
                    }
                    navController.navigate("studentProfile")
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
    }
}



