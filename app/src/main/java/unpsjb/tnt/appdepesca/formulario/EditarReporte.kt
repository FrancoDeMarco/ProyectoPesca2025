package unpsjb.tnt.appdepesca.formulario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.reportes.ReportViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.TextStyle
import unpsjb.tnt.appdepesca.login.HeaderImage
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

/****El EditarReporte, recibe los view model y el nav para trabajar sobre ellos.*/
/*@Composable
fun EditarReporte(
    reporteViewModel: ReporteViewModel,
    reportViewModel: ReportViewModel,
    navController: NavController
) {
    val state = reportViewModel.state
    val showDialog = remember { mutableStateOf(false) }
    val isLoading: Boolean by reporteViewModel.isLoading.observeAsState(initial = false)
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
            HeaderImage(size = 200.dp) // usa un tamaño personalizado
            TituloEditar()
            NombreReporte(reportViewModel, state, isTitleValid)
            DescripcionReporte(reportViewModel, state, isDescriptionValid)
            FechaReporte(reportViewModel, dateState, isDateValid)
            VolverButton(navController, showDialog)
            EditarButton(enabled = formValido) {
                reportViewModel.createReport()
                navController.navigate("reportes")
            }
        }
    }
}


//////////////TITULO/////////////////////
@Composable
fun TituloEditar() {
    Text(
        text = "Editar Reporte",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

//////////////BOTON EDITAR REPORTE//////
@Composable
fun EditarButton(enabled: Boolean, onClick: () -> Unit) {
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
        Text("Editar Reporte")
    }
}*/