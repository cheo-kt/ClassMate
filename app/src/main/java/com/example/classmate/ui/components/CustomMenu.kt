package com.example.classmate.ui.components

import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuItemWithSeparator(label: String, onClick: () -> Unit, onDismiss: () -> Unit) {
    DropdownMenuItem(onClick = {
        onClick()
        onDismiss() // Call onDismiss to close the dropdown
    }) {
        Text(label)
    }
    Divider(color = Color.Black, thickness = 1.dp) // Separator
}