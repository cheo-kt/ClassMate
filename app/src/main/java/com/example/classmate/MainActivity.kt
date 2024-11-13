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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.classmate.ui.screens.BroadcastDecisionScreen
import com.example.classmate.ui.screens.CalendarStudentScreen
import com.example.classmate.ui.screens.HomeMonitorScreen
import com.example.classmate.ui.screens.HomeStudentScreen
import com.example.classmate.ui.screens.IntroductionsMonitorScreen
import com.example.classmate.ui.screens.IntroductionsStudentScreen
import com.example.classmate.ui.screens.MonitorEditScreen
import com.example.classmate.ui.screens.MonitorProfileScreen
import com.example.classmate.ui.screens.MonitorSignUpScreen
import com.example.classmate.ui.screens.MonitorStudentScreen
import com.example.classmate.ui.screens.NotificationStudentScreen
import com.example.classmate.ui.screens.OpinionStudentScreen
import com.example.classmate.ui.screens.RequestBroadcastStudentScreen
import com.example.classmate.ui.screens.StudentEditScreen
import com.example.classmate.ui.screens.StudentMonitorSigninScreen
import com.example.classmate.ui.screens.StudentProfileScreen
import com.example.classmate.ui.components.deserializeListAppointment
import com.example.classmate.ui.components.deserializeListRequestBroadcast
import com.example.classmate.ui.screens.AppoimentStudentScreen
import com.example.classmate.ui.screens.DayOfCalendarStudentScreen
import com.example.classmate.ui.screens.PreviewMonitorProfile
import com.example.classmate.ui.screens.RequestBroadcastStudentView
import com.example.classmate.ui.screens.StudentSignupScreen
import com.example.classmate.ui.screens.UnicastMonitoringScreen
import com.example.classmate.ui.screens.tutorialStudent.guia6StudentScreen
import com.example.classmate.ui.screens.tutorialStudent.guia7StudentScreen
import com.example.classmate.ui.theme.ClassMateTheme
import com.example.classmate.ui.viewModel.StudentSignupViewModel
import com.google.gson.Gson
import guia1StudentScreen
import guia2StudentScreen
import guia3StudentScreen
import guia4StudentScreen
import guia5StudentScreen


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
    NavHost(navController = navController, startDestination = "guia1Student" +
            "") {
        composable("signup") { StudentSignupScreen(navController) } //Registro estudiante
        composable("signing") { StudentMonitorSigninScreen(navController) } //Login
        composable("introductionStudent") { IntroductionsStudentScreen(navController) } //introducción Estudiante
        composable("HomeStudentScreen") { HomeStudentScreen(navController) } //HomeStrudiante
        composable("unicastMonitoring?monitor={monitor}&student={student}&materia={materia}", arguments = listOf(    //Invocación alternativa, de la manera con / daño la ruta
            navArgument("monitor"){type= NavType.StringType}, // Pantalla de solicitud de monitoria a monitor en particular.
            navArgument("student"){type= NavType.StringType},
            navArgument("materia"){type= NavType.StringType},
        )) {entry->
            val monitor =entry.arguments?.getString("monitor") //Recojo el argumento de la panatalla donde es creado y luego si lo mando al constructor de mi otra clase
            val student =entry.arguments?.getString("student")
            val materia = entry.arguments?.getString("materia")
            UnicastMonitoringScreen(navController,monitor,student,materia) } //Muestro la pantalla, estoy pasando la password por parametro para mostrarla en la otra pantalla.
        composable("selectMonitorStudent"){MonitorStudentScreen(navController)} //Selección de registro
        composable("HomeMonitorScreen"){ HomeMonitorScreen(navController)} //HomeMonitor
        composable("introductionMonitor") { IntroductionsMonitorScreen(navController) } //introducción Estudiante
        composable("monitorProfile") { MonitorProfileScreen(navController) } //Perfil Monitor
        composable("monitorEdit"){ MonitorEditScreen(navController) } //Editar perfil Monitor
        composable("studentProfile") { StudentProfileScreen(navController) } // Perfil del estudiante
        composable("studentEdit"){ StudentEditScreen(navController)} // Perfil del montior
        composable("signupMonitor"){ MonitorSignUpScreen(navController) }
        composable("requestBroadcast") { RequestBroadcastStudentScreen(navController) }
        composable("previewMonitorProfile?monitor={monitor}&student={student}&materia={materia}", arguments = listOf(    //Invocación alternativa, de la manera con / daño la ruta
            navArgument("monitor"){type= NavType.StringType}, // Pantalla de solicitud de monitoria a monitor en particular.
            navArgument("student"){type= NavType.StringType},
            navArgument("materia"){type= NavType.StringType},
        )) {entry->
            val monitor =entry.arguments?.getString("monitor") //Recojo el argumento de la panatalla donde es creado y luego si lo mando al constructor de mi otra clase
            val student =entry.arguments?.getString("student")
            val materia = entry.arguments?.getString("materia")
            PreviewMonitorProfile(navController, monitor , student , materia )
        }

        composable("notificationStudentPrincipal"){ NotificationStudentScreen(navController) }
        composable("OpinionStudent?notification={notification}", arguments =
        listOf(navArgument("notification"){ type=NavType.StringType}
        )){ entry ->
            val notification =entry.arguments?.getString("notification")
            OpinionStudentScreen(navController, notification)
        }
        composable("CalendarStudent"){ CalendarStudentScreen(navController) }
        composable("DayOfCalendar?requestsForDay={requestsForDay}&appointmentsForDay={appointmentsForDay}", arguments = listOf(
            navArgument("requestsForDay") { type = NavType.StringType },
            navArgument("appointmentsForDay") { type = NavType.StringType }
        )) { entry ->
            val requestsForDayJson = entry.arguments?.getString("requestsForDay")
            val appointmentsForDayJson = entry.arguments?.getString("appointmentsForDay")

            // Deserializar los datos en listas específicas
            val requestsForDay = deserializeListRequestBroadcast(requestsForDayJson)
            val appointmentsForDay = deserializeListAppointment(appointmentsForDayJson)

            // Navegar a la pantalla con los datos deserializados
            DayOfCalendarStudentScreen(
                navController,
                requestsForDay,
                appointmentsForDay
            )
        }
        composable("requestBroadcastView?requestBroadcast={requestBroadcast}", arguments = listOf(
            navArgument("requestBroadcast") { type = NavType.StringType }
        )) { entry ->
            val jsonRequestBroadcast = entry.arguments?.getString("requestBroadcast")
                RequestBroadcastStudentView(navController, jsonRequestBroadcast)
        }
        composable("AppointmentStudentView?appointment={appointment}", arguments = listOf(
            navArgument("appointment"){type= NavType.StringType}
        )) {entry->
            val jsonRequestBroadcast =entry.arguments?.getString("appointment")
            AppoimentStudentScreen(navController,jsonRequestBroadcast)
        }


        composable("DecisionMonitor?request={request}&monitor={monitor}",
        arguments = listOf(
            navArgument("request"){type= NavType.StringType},
            navArgument("monitor"){type= NavType.StringType}
        )) {entry->
        val request =entry.arguments?.getString("request")
        val monitor =entry.arguments?.getString("monitor")
        BroadcastDecisionScreen(navController,request,monitor) }
        //GUIA
        composable("guia1Student") { guia1StudentScreen(navController) }
        composable("guia2Student") { guia2StudentScreen(navController) }
        composable("guia3Student") { guia3StudentScreen(navController) }
        composable("guia4Student") { guia4StudentScreen(navController) }
        composable("guia5Student") { guia5StudentScreen(navController) }//Registro estudiante
        composable("guia6Student") { guia6StudentScreen(navController) }
        composable("guia7Student") { guia7StudentScreen(navController) }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClassMateTheme {
        App()
    }
}