import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
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
        modifier = Modifier.widthIn(max = 300.dp)  // Limita el ancho máximo para evitar que ocupe demasiado espacio
    )
    DropdownMenu(
        expanded = expanded && filteredSubjects.isNotEmpty(),
        onDismissRequest = { expanded = false }
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

