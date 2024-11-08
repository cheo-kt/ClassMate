package com.example.classmate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(visible: Boolean, timePickerState: TimePickerState, dismiss: () -> Unit) {
    if (visible) {
        Dialog(
            onDismissRequest = {

            }
        ) {
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos
                ) {
                    TimePicker(state = timePickerState)
                    Button(
                        onClick = {
                            dismiss()
                        },
                        modifier = Modifier.fillMaxWidth(0.5f) // Ajusta el tamaño del botón
                    ) {
                        Text(
                            text = "Confirmar",
                            color = Color.White // Cambia el color del texto a blanco
                        )
                    }
                }
            }


        }
    }
}
