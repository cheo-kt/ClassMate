package com.example.classmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.classmate.ui.screens.RequestBroadcastStudentView
import com.example.classmate.ui.screens.StudentSignupScreen
import com.example.classmate.ui.screens.UnicastMonitoringScreen
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
    NavHost(navController = navController, startDestination = "signing") {
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
        composable("requestBroadcast?student={student}", arguments = listOf(
            navArgument("student"){type= NavType.StringType}
        )) { entry ->
            val student =entry.arguments?.getString("student")
            RequestBroadcastStudentScreen(navController,student)
        }
        composable("notificationStudentPrincipal"){ NotificationStudentScreen(navController) }
        composable("OpinionStudent?notification={notification}", arguments =
        listOf(navArgument("notification"){ type=NavType.StringType}
        )){ entry ->
            val notification =entry.arguments?.getString("notification")
            OpinionStudentScreen(navController, notification)
        }

        composable("CalendarStudent?student={student}", arguments = listOf(
            navArgument("student"){type= NavType.StringType}
        )){  entry ->
            val student =entry.arguments?.getString("student")
            CalendarStudentScreen(navController,student)
        }
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
        composable("AppoimentStudentView?appointment={appointment}}", arguments = listOf(
            navArgument("appointment"){type= NavType.StringType}
        )) {entry->
            val appointment =entry.arguments?.getString("appointment")
            AppoimentStudentScreen(navController,appointment) }


        composable("DecisionMonitor?request={request}&monitor={monitor}",
        arguments = listOf(
            navArgument("request"){type= NavType.StringType},
            navArgument("monitor"){type= NavType.StringType}
        )) {entry->
        val request =entry.arguments?.getString("request")
        val monitor =entry.arguments?.getString("monitor")
        BroadcastDecisionScreen(navController,request,monitor) }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClassMateTheme {
        App()
    }
}