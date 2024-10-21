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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun StudentProfileScreen(navController: NavController, authViewModel: StudentProfileViewModel = viewModel()){
    authViewModel.showStudentInformation()
    val student: Student? by authViewModel.student.observeAsState(initial = null)
    var image = student?.photo
    val scrollState = rememberScrollState()
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
                Button(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(Color.Transparent),
                    onClick = {
                        navController.navigate("HomeStudentScreen")
                    }) {
                    Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null )
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
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Acerca de mi: ")
                    Spacer(modifier = Modifier.height(16.dp))
                    student?.let {
                        Text(text = it.description)
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
                        Text(text = it.phone)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = it.email)
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
