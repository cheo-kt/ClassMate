package com.example.classmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.classmate.domain.model.Student
import com.example.classmate.ui.screens.HomeMonitorScreen
import com.example.classmate.ui.screens.HomeStudentScreen
import com.example.classmate.ui.screens.IntroductionsScreen
import com.example.classmate.ui.screens.MonitorSignUpScreen
import com.example.classmate.ui.screens.MonitorStudentScreen
import com.example.classmate.ui.screens.StudentMonitorSigninScreen
import com.example.classmate.ui.screens.StudentSignupScreen
import com.example.classmate.ui.theme.ClassMateTheme
import com.example.classmate.ui.viewModel.StudentSignupViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassMateTheme {
                App()
            }
        }
    }
}
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "signing") {
        composable("signup") { StudentSignupScreen(navController) } //Registro estudiante
        composable("signing") { StudentMonitorSigninScreen(navController) } //Login
        composable("introduction") { IntroductionsScreen(navController) } //introducción
        composable("HomeStudentScreen") { HomeStudentScreen(navController) } //HomeStrudiante
        composable("signupMonitor"){ MonitorSignUpScreen(navController) } //Registro monitor
        composable("selectMonitorStudent"){MonitorStudentScreen(navController)} //Selección de registro
        composable("HomeStudentScreen"){ HomeMonitorScreen(navController)} //HomeMonitor
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClassMateTheme {
        App()
    }
}