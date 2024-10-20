import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.classmate.domain.model.Subjects

@Composable
fun PredictiveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val subjects = Subjects().materias

    val filteredSubjects = if (value.length >= 3) {
        subjects.filter { it.contains(value, ignoreCase = true) }
    } else {
        emptyList()
    }
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            expanded = it.length >= 3
        },
        label = { Text(label) },
        modifier = Modifier.widthIn(max = 300.dp)
    )
    DropdownMenu(
        expanded = expanded && filteredSubjects.isNotEmpty(),
        onDismissRequest = { expanded = false },
        properties = PopupProperties(focusable = false),
    ) {
        filteredSubjects.forEach { suggestion ->
            DropdownMenuItem(
                {
                    Text(text = suggestion)
                }, onClick = {
                    onValueChange(suggestion)
                    expanded = false
                }
            )
        }
    }
}

