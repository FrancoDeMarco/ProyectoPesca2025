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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.TextStyle
import unpsjb.tnt.appdepesca.reportes.ReportState
import kotlin.Boolean

/* COLORES

// Fondo general
val BackgroundColor = Color(0xFF1B2B24)

/* Botones
val ButtonActiveColor = Color(0xFF3E8B75)       // Botón habilitado (verde)
val ButtonDisabledColor = Color(0xFF5D776C)     // Botón deshabilitado (gris oscuro)
val ButtonTextEnabled = Color.White             // Texto blanco cuando está habilitado
val ButtonTextDisabled = Color(0xFFAAAAAA)      // Texto gris claro cuando está deshabilitado
*/
// Texto general
val PrimaryTextColor = Color(0xFF3E8B75)        // Color verde para títulos o palabras importantes

 */

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
    var isDateValid = remember { mutableStateOf(false) }
    val isTitleValid = remember { mutableStateOf(false) }
    var isDescriptionValid = remember { mutableStateOf(false) }
    val formValido = isDateValid.value && isTitleValid.value && isDescriptionValid.value

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1B2B24))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))  // Espacio de 24dp entre los campos
            TituloReporte()
            Spacer(modifier = Modifier.height(24.dp))  // Espacio de 24dp entre los campos
            NombreReporte(reportViewModel, state, isTitleValid)
            DescripcionReporte(reportViewModel, state, isDescriptionValid)
            FechaReporte(reportViewModel, dateState, isDateValid, navController)
            Spacer(modifier = Modifier.height(24.dp))  // Espacio de 24dp entre los campos
            VolverButton(navController, showDialog)
            Spacer(modifier = Modifier.height(24.dp))  // Espacio de 24dp entre los campos
            AgregarButton(enabled = formValido) {
                reportViewModel.createReport()
                navController.navigate("reportes")
            }
        }
    }
}


//////////////TITULO/////////////////////
@Composable
fun TituloReporte() {
    Text(
        text = "Nuevo Reporte",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
//////////////NOMBRE DEL REPORTE/////////
@Composable
fun NombreReporte(
    reportViewModel: ReportViewModel,
    state: ReportState,
    isTitleValid: MutableState<Boolean>
){
    TextField(
        value = state.reportTitle,
        onValueChange = { newValue ->
            reportViewModel.changeTitle(newValue)
            isTitleValid.value = newValue.trim().isNotEmpty()
        },
        placeholder = { Text(text = "Nombre del reporte") }
    )
}
//////////////DESCRIPCION DEL REPORTE////
@Composable
fun DescripcionReporte(
    reportViewModel: ReportViewModel,
    state: ReportState,
    isDescriptionValid: MutableState<Boolean>
){
    TextField(
        value = state.reportDescription,
        onValueChange = { newValue ->
            reportViewModel.changeDescription(newValue)
            isDescriptionValid.value = newValue.trim().isNotEmpty()
        },
        placeholder = { Text(text = "Descripción") }
    )
}

//////////////FECHA DEL REPORTE//////////
@Composable
fun FechaReporte(
    reportViewModel: ReportViewModel,
    dateState: MutableState<TextFieldValue>,
    isDateValid: MutableState<Boolean>,
    navController: NavController
){
    TextField(
        value = dateState.value.text,
        onValueChange = { newValue ->
            dateState.value = TextFieldValue(newValue)
            reportViewModel.changeDate(newValue)

            // Validar el formato de fecha utilizando una expresión regular
            val regex =
                """(0[1-9]|[12]\d|3[01])/(0[1-9]|1[0-2])/((19|20)\d\d)""".toRegex()
            isDateValid.value = regex.matches(newValue) && isValidDate(newValue)
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
}
//////////////BOTON VOLVER///////////////
@Composable
fun VolverButton(
    navController: NavController,
    showDialog: MutableState<Boolean>
) {
    Button(
        onClick = {
            showDialog.value = true
            navController.navigate("reportes")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E8B75))
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver"
        )
        Text("Volver")
    }
}

//////////////BOTON AGREGAR REPORTE//////
@Composable
fun AgregarButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3E8B75),             // Botón habilitado (verde)
            disabledContainerColor = Color(0xFF5D776C),     // Botón deshabilitado (gris oscuro)
            contentColor = Color.White,                     // texto en blanco
            disabledContentColor = Color(0xFFAAAAAA)        // texto gris claro cuando está deshabilitado
        )
    ) {
        Text(
            text = "Agregar Reporte",
            fontSize = 20.sp
        )
    }
}


/////////////////Validador de fecha////////////
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

