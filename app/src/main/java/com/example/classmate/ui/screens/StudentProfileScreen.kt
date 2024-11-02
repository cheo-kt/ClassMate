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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import com.example.classmate.R
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.viewModel.StudentProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentProfileScreen(navController: NavController, authViewModel: StudentProfileViewModel = viewModel()){
    authViewModel.showStudentInformation()
    val studentState by authViewModel.studentState.observeAsState()
    val student: Student? by authViewModel.student.observeAsState(initial = null)
    var image = student?.photo
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3F21DB))
                    .height(120.dp),

            ) {
                IconButton(
                    onClick = {
                        navController.navigate("signing")
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
                    .background(Color(0xFFCCD0CF))
            )
            {
                student?.let {
                    image = it.photo
                    if (studentState==2){
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Hay problemas para conectarse con el servidor, revise su conexión")
                        }
                    }
                }
                Image(
                    modifier = Modifier
                        .size(200.dp) // Tamaño de la imagen
                        .clip(CircleShape) // Hace que la imagen sea circular
                        .size(200.dp)
                        .fillMaxSize()
                        ,
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(image, error = painterResource(R.drawable.botonestudiante)),
                    contentScale = ContentScale.Crop
                )

            }
            Spacer(modifier = Modifier.height(30.dp))
            student?.let {
                Text(text = it.name + " " + it.lastname)
            } ?: run {
                Text(text = "NO_NAME")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFCCD0CF)),
                contentAlignment = Alignment.Center

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top, // Alinea los elementos en la parte superior
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "  Acerca de mi: ", modifier = Modifier.offset(0.dp,3.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    student?.let {
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
                    student?.let {
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
            Button( onClick = {
                navController.navigate("studentEdit")
            }) {
                Text(text = "Editar perfil")
            }
        }
    }
}
