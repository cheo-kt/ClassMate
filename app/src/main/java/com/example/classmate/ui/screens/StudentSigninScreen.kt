package com.example.classmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.components.CustomTextField
import com.example.classmate.ui.viewModel.StudentSigninViewModel
import com.example.classmate.ui.viewModel.StudentSignupViewModel

@Composable
fun StudentSigninScreen (navController: NavController, authViewModel: StudentSigninViewModel = viewModel()){

    val authState by authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Scaffold(modifier = Modifier.fillMaxSize()) {  innerpadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerpadding)){
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isPassword = true)
            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Acceder como:",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )


            if(authState == 1){
                CircularProgressIndicator()
            }else if(authState == 2){
                Text(text = "Hubo un error, que no podemos ver todavia")
            }else if (authState == 3){
                navController.navigate("profile")
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








        }

    }

}