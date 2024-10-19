package com.example.classmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.classmate.ui.screens.HomeStudentScreen
import com.example.classmate.ui.screens.IntroductionsScreen
import com.example.classmate.ui.screens.StudentSigninScreen
import com.example.classmate.ui.screens.StudentSignupScreen

import com.example.classmate.ui.theme.ClassMateTheme


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
    NavHost(navController = navController, startDestination = "introduction") {
        composable("signup") { StudentSignupScreen(navController) }
        composable("signing") { StudentSigninScreen(navController) }
        composable("introduction") { IntroductionsScreen(navController) }
        composable("HomeStudentScreen") { HomeStudentScreen(navController) }

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClassMateTheme {
        App()
    }
}