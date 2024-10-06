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
import com.example.classmate.ui.theme.ClassMateTheme
import com.example.classmate.viewModel.StudentSignupViewModel


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
    NavHost(navController = navController, startDestination = "profile") {
        composable("signup") { StudentSignupScreen(navController) }

    }
}

@Composable
fun StudentSignupScreen(navController: NavController, studentSignupViewModel: StudentSignupViewModel = viewModel()) {

    val authState by studentSignupViewModel.authState.observeAsState()

    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Para manejar el mensaje de error

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = Color(0xFF3F51B5),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(4.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CustomTextField(value = nombres, onValueChange = { nombres = it }, label = "Nombres")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(value = apellidos, onValueChange = { apellidos = it }, label = "Apellidos")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(value = password, onValueChange = { password = it }, label = "Contraseña", isPassword = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomTextField(value = confirmarContrasena, onValueChange = { confirmarContrasena = it }, label = "Confirmar Contraseña", isPassword = true)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar el mensaje de error si las contraseñas no coinciden
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (password == confirmarContrasena) {
                                // Si las contraseñas coinciden, proceder con el registro
                                studentSignupViewModel.signup(
                                    Student("", nombres, apellidos, telefono, email),
                                    password
                                )
                            } else {
                                // Si las contraseñas no coinciden, mostrar el mensaje de error
                                errorMessage = "Las contraseñas no son iguales, intenta de nuevo"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text("Registrarse", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "¿Ya tienes una cuenta? ¡Inicia sesión!",
                        modifier = Modifier.clickable { /* Navegar a la pantalla de login */ },
                        color = Color(0xFF3F51B5),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (authState == 1) {
            CircularProgressIndicator()
        } else if (authState == 2) {
            Text("Hubo un error", color = Color.Red)
        } else if (authState == 3) {
            navController.navigate("profile")
        }
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default
    )
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClassMateTheme {
        Greeting("Android")
    }
}