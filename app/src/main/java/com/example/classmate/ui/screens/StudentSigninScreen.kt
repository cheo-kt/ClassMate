package com.example.classmate.ui.screens
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.StudentSigninViewModel
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.classmate.R


@Composable
fun StudentSigninScreen (navController: NavController, authViewModel: StudentSigninViewModel = viewModel()){



    val authState by authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Scaffold(modifier = Modifier.fillMaxSize()) {  innerpadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center

            ) {
                Image(
                    painter = painterResource(id = R.drawable.sssss),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.classmatelogo),
                    contentDescription = null,
                    modifier = Modifier.size(350.dp).offset(y = (-50).dp),
                )

            }

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 50.sp, fontWeight =FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp),
                textAlign = TextAlign.Center
            )



            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco

            Box(modifier = Modifier.padding(horizontal = 32.dp)){
                Column {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp, fontWeight =FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                    CustomTextField(value = email, onValueChange = { email = it }, label = "")
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 15.sp, fontWeight =FontWeight.Bold),
                        color = Color.Black,
                        textAlign = TextAlign.Left
                    )
                    CustomTextField(value = password, onValueChange = { password = it }, label = "", isPassword = true)
                }
            }

            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco
            Box(modifier = Modifier.padding(horizontal = 32.dp)){
                Column {
                    Text(
                        text = "Acceder como:",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Left
                    )

                    Button(
                        onClick = {

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF209619))
                    ) {
                        Text("Monitor", color = Color.White)
                    }
                    Button(
                        onClick = {
                            authViewModel.signin(email, password)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text("Estudiante", color = Color.White)
                    }

                    if(authState == 1){
                        CircularProgressIndicator()
                    }else if(authState == 2){
                        Text(text = "Hubo un error, que no podemos ver todavia")
                    }else if (authState == 3){
                        navController.navigate("profile")
                    }
                }

            }
            Box(modifier = Modifier
                .weight(0.1f)) // Espacio en blanco
            Text(
                text = "¿Aún no tienes cuenta? ¡Regístrate!",
                modifier = Modifier.clickable {navController.navigate("signup") },
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )

        }

    }

}




