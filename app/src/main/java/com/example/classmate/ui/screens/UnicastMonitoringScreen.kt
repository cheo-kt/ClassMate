package com.example.classmate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classmate.App
import com.example.classmate.R
import com.example.classmate.ui.theme.ClassMateTheme
import com.example.classmate.ui.viewModel.UnicastMonitoringViewModel
import androidx.compose.material.icons.filled.Close
import kotlinx.coroutines.launch


@Composable
fun UnicastMonitoringScreen(navController: NavController, unicastMonitoringViewModel: UnicastMonitoringViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val modalidadSeleccionada = remember { mutableStateOf("") }
    val direccion = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    val horaInicio = remember { mutableStateOf("") }
    val horafin = remember { mutableStateOf("") }
    val notas = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Sección superior fija
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.encabezadorequest),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { navController.navigate("monitorProfile") },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.classmatelogo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Sección desplazable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Aquí puedes agregar los componentes de tu pantalla desplazable, según tu imagen

                // Opciones de modalidad (Virtual, Presencial, etc.)
                Text("¡Haz tu solicitud!", style = MaterialTheme.typography.titleLarge)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Opciones de modalidad
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = modalidadSeleccionada.value == "Virtual",
                            onClick = { modalidadSeleccionada.value = "Virtual" }
                        )
                        Text(
                            text = "Virtual",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = modalidadSeleccionada.value == "Presencial",
                            onClick = { modalidadSeleccionada.value = "Presencial" }
                        )
                        Text(
                            text = "Presencial",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        // Campo para ingresar dirección (solo se muestra si es presencial)
                        if (modalidadSeleccionada.value == "Presencial") {
                            OutlinedTextField(
                                value = direccion.value,
                                onValueChange = { nuevaDireccion -> direccion.value = nuevaDireccion },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .width(200.dp),
                                label = { Text("Dirección") }, // Etiqueta para el campo de dirección
                                readOnly = false // Permite que el usuario edite el campo
                            )
                        }
                    }
                }

                // Fecha y hora de solicitud
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Selector de fecha
                    OutlinedTextField(
                        value = fecha.value,
                        onValueChange = { nuevaFecha -> fecha.value = nuevaFecha },
                        label = { Text("Fecha (dd/mm/yyyy)") },
                        modifier = Modifier.weight(1f),
                        readOnly = false
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Selector de hora inicio
                    OutlinedTextField(
                        value = horaInicio.value,
                        onValueChange = { nuevaHora -> horaInicio.value = nuevaHora },
                        label = { Text("Hora de inicio (HH:mm)") },
                        modifier = Modifier.weight(1f),
                        readOnly = false
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    TextField(
                        value = horafin.value,
                        onValueChange = {nuevaHora -> horafin.value = nuevaHora},
                        label = { Text("Hora de inicio (HH:mm)") },
                        modifier = Modifier.weight(1f),
                        readOnly = false
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notas
                OutlinedTextField(
                    value = notas.value,
                    onValueChange = { nuevaNota -> notas.value = nuevaNota },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = false // Permite que el usuario edite el campo
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de confirmar o cancelar
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if(modalidadSeleccionada.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("modalidad no seleccionada")
                                }
                            }
                            if(modalidadSeleccionada.value == "Presencial" && direccion.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("no se ha especificado la dirección")
                                }
                            }
                            if(fecha.value.isEmpty() || horaInicio.value.isEmpty() || horafin.value.isEmpty()){
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("la fecha u hora no se han definido.")
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirmar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate("monitorProfile") },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

