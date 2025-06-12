package unpsjb.tnt.appdepesca.formulario


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.reportes.ReportViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    navController: NavHostController
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text("¿Está seguro que desea cargar estos datos?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    navController.navigate("reportes")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("No")
            }
        }
    )
}

/****El FormularioScreen, recibe los view model y el nav para trabajar sobre ellos.*/
@Composable
fun FormularioScreen(
    formularioViewModel: FormularioViewModel,
    reportViewModel: ReportViewModel,
    navController: NavController
) {
    val state = reportViewModel.state
    val showDialog = remember { mutableStateOf(false) }
    val isLoading: Boolean by formularioViewModel.isLoading.observeAsState(initial = false)
    val dateState = remember { mutableStateOf(TextFieldValue(state.reportDate)) }
    var isDateValid by remember { mutableStateOf(false) }
    var isTitleValid by remember { mutableStateOf(false) }
    var isDescriptionValid by remember { mutableStateOf(false) }

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF9EEE9E))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Formulario", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            TextField(
                value = state.reportTitle,
                onValueChange = { newValue ->
                    reportViewModel.changeTitle(newValue)
                    isTitleValid = newValue.trim().isNotEmpty()
                },
                placeholder = { Text(text = "Nombre del reporte") }
            )
            TextField(
                value = state.reportDescription,
                onValueChange = { newValue ->
                    reportViewModel.changeDescription(newValue)
                    isDescriptionValid = newValue.trim().isNotEmpty()
                },
                placeholder = { Text(text = "Descripción") }
            )
            TextField(
                value = dateState.value.text,
                onValueChange = { newValue ->
                    dateState.value = TextFieldValue(newValue)
                    reportViewModel.changeDate(newValue)

                    // Validar el formato de fecha utilizando una expresión regular
                    val regex = """(0[1-9]|[12]\d|3[01])/(0[1-9]|1[0-2])/((19|20)\d\d)""".toRegex()
                    isDateValid = regex.matches(newValue) && isValidDate(newValue)
                },
                placeholder = { Text(text = "dd/MM/yyyy") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        navController.navigate("reportes")
                    }
                )
            )

            Button(
                onClick = {
                    showDialog.value = true
                    navController.navigate("reportes")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC3C3C))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    run {
                        reportViewModel.createReport()
                        navController.navigate("reportes")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDateValid && isTitleValid && isDescriptionValid) Color(0xFF59FC3C) else Color.Gray
                ),
                enabled = isDateValid && isTitleValid && isDescriptionValid
            ) {
                Text(text = "Agregar Reporte")
            }

        }
    }
}

private fun isValidDate(dateString: String): Boolean {
    val parts = dateString.split('/')
    if (parts.size != 3) return false

    val day = parts[0].toIntOrNull()
    val month = parts[1].toIntOrNull()
    val year = parts[2].toIntOrNull()

    if (day == null || month == null || year == null) return false

    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> return false
    }

    return day in 1..daysInMonth && year in 1900..2050
}

