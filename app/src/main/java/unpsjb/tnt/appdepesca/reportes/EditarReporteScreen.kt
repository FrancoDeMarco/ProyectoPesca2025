package unpsjb.tnt.appdepesca.reportes

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import coil.compose.rememberAsyncImagePainter
import unpsjb.tnt.appdepesca.login.HeaderImage
import java.util.Calendar
import kotlin.Boolean
import androidx.core.net.toUri

/****El FormularioScreen, recibe los view model y el nav para trabajar sobre ellos.*/
@Composable
fun EditarReporteScreen(
    reporteViewModel: ReporteViewModel,
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
) {
    val state = listadoReportesViewModel.state
    val isLoading: Boolean by reporteViewModel.isLoading.observeAsState(initial = false)
    val dateState = remember { mutableStateOf(TextFieldValue(state.reportDate)) }
    // Estados de validación
    val isTitleValid = remember { mutableStateOf(false) }
    val isDescriptionValid = remember { mutableStateOf(false) }
    val isDateValid = remember { mutableStateOf(false) }
    val formValido by remember(state) {
        derivedStateOf {
            state.reportDate.isNotBlank() &&
                    state.reportTitle.isNotBlank() &&
                    state.reportDescription.isNotBlank() &&
                    !state.reportImagenUri.isNullOrBlank() &&
                    state.reportModalidad != null // Modalidad es obligatorio
        }
    }
    // Inicialización de los estados cuando se abre la pantalla
    LaunchedEffect(Unit) {
        isTitleValid.value = state.reportTitle.isNotBlank()
        isDescriptionValid.value = state.reportDescription.isNotBlank()
        isDateValid.value = state.reportDate.isNotBlank()
    }
    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1B2B24))
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderImage(size = 200.dp) // usa un tamaño personalizado
            TituloEditar()
            EditarNombreReporte(listadoReportesViewModel, state, isTitleValid)
            EditarDescripcionReporte(listadoReportesViewModel, state, isDescriptionValid)
            EditarFechaReporte(listadoReportesViewModel, dateState, isDateValid)
            SelectorModalidad(modalidadSeleccionada = state.reportModalidad, onSeleccionar = { listadoReportesViewModel.changeModalidad(it)})
            EditarImagenReporte(viewModel = listadoReportesViewModel)
            SiguienteEditar(navController, modifier = Modifier, enabled = formValido)
            BotonVolver(navController)
        }

    }
}

//////////////NOMBRE DEL REPORTE/////////
@Composable
fun EditarNombreReporte(
    listadoReportesViewModel: ListadoReportesViewModel,
    state: ReportState,
    isTitleValid: MutableState<Boolean>
){
    OutlinedTextField(
        value = state.reportTitle,
        onValueChange = { newValue ->
            listadoReportesViewModel.changeTitle(newValue)
            isTitleValid.value = newValue.trim().isNotEmpty()
        },
        placeholder = { Text(text = "Nombre del reporte") },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )
    )
}
//////////////DESCRIPCION DEL REPORTE////
@Composable
fun EditarDescripcionReporte(
    listadoReportesViewModel: ListadoReportesViewModel,
    state: ReportState,
    isDescriptionValid: MutableState<Boolean>
){
    OutlinedTextField(
        value = state.reportDescription,
        onValueChange = { newValue ->
            listadoReportesViewModel.changeDescription(newValue)
            isDescriptionValid.value = newValue.trim().isNotEmpty()
        },
        placeholder = { Text(text = "Descripción") },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )
    )
}

//////////////FECHA DEL REPORTE//////////
@Composable
fun EditarFechaReporte(
    listadoReportesViewModel: ListadoReportesViewModel,
    dateState: MutableState<TextFieldValue>,
    isDateValid: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    OutlinedTextField(
        value = dateState.value.text,
        onValueChange = {}, // no editable
        readOnly = true,
        placeholder = { Text("Fecha") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val selectedDate = "%02d/%02d/%04d".format(
                            selectedDayOfMonth, selectedMonth + 1, selectedYear
                        )
                        dateState.value = TextFieldValue(selectedDate)
                        listadoReportesViewModel.changeDate(selectedDate)
                        isDateValid.value = true
                    },
                    year, month, day
                ).apply {
                    datePicker.maxDate = calendar.timeInMillis
                }.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = Color.White
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )
    )
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

/////////////////EDITAR IMAGEN//////////////////////
@Composable
fun EditarImagenReporte(viewModel: ListadoReportesViewModel) {
    val state = viewModel.state
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.changeImage(context, it)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mostrar la imagen si existe
        state.reportImagenUri?.let { uriString ->
            val imageUri = uriString.toUri()
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Imagen del reporte",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            )
        }
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E8B75))
        ) {
            Text("Cambiar Imagen")
        }
    }
}

@Composable
fun SiguienteEditar(
    navController: NavController,
    enabled: Boolean,
    modifier: Modifier
){
    Button(
        onClick = { navController.navigate("seleccionar_ubicacion_editar") },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color(0xFF3E8B75) else Color (0xFF2E2E2E),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF2E2E2E),
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (!enabled) BorderStroke(2.dp, Color.Gray) else null
    ) {
        Text(
            text = "Seleccionar Ubicación",
            fontSize = 20.sp // tamaño
        )
    }
}
