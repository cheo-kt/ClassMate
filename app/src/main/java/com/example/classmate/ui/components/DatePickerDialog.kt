package com.example.classmate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(visible: Boolean, datePickerState: DatePickerState, dismiss: () -> Unit) {
    if (visible) {
        Dialog(
            onDismissRequest = {


            }
        ) {

            Card( ) {


                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally // Centrar el contenido
                ) {

                    DatePicker(state = datePickerState)
                    Button(
                        onClick = {
                            dismiss()
                        },
                        modifier = Modifier.fillMaxWidth(0.5f) // Ajusta el tamaño del botón si lo necesitas
                    ) {
                        Text(
                            text = "Confirmar",
                            color = Color.White // Cambiar el color del texto a blanco
                        )
                    }
                }
            }


        }
    }
}

