package unpsjb.tnt.appdepesca.reportes

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition.Start.position
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import unpsjb.tnt.appdepesca.login.HeaderImage
import java.util.Calendar
import kotlin.Boolean

/****El FormularioScreen, recibe los view model y el nav para trabajar sobre ellos.*/
@Composable
fun EditarReporteScreen(
    //TODO agregar mapa.
    //TODO Cuando edito un reporte y no selecciono ubicación, se borra el mapa del detalle.
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
    val formValido = isTitleValid.value && isDescriptionValid.value

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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderImage(size = 200.dp) // usa un tamaño personalizado
            TituloEditar()
            EditarNombreReporte(listadoReportesViewModel, state, isTitleValid)
            EditarDescripcionReporte(listadoReportesViewModel, state, isDescriptionValid)
            EditarFechaReporte(listadoReportesViewModel, dateState, isDateValid)
            EditarImagenReporte(viewModel = listadoReportesViewModel)
            EditarMapaReporte(listadoReportesViewModel)
            EditarButton(enabled = formValido) {
                listadoReportesViewModel.updateReport()
                navController.navigate("reportes")
            }
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


//////////////BOTON AGREGAR REPORTE//////
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
    val context = LocalContext.current
    val state = viewModel.state
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.changeImage(it)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mostrar la imagen si existe
        state.reportImagenUri?.let { uriString ->
            val imageUri = Uri.parse(uriString)
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

/////////////////EDITAR UBICACIÓN//////////////////////
@OptIn(MapsComposeExperimentalApi::class)
val LatLngSaver: Saver<LatLng, Pair<Double, Double>> = Saver(
    save = { Pair(it.latitude, it.longitude)},
    restore = { LatLng(it.first, it.second) }
)
@Composable
fun EditarMapaReporte(
    viewModel: ListadoReportesViewModel
) {
    val state = viewModel.state
    //Solo dibujamos el mapa cuando ya existen coordenadas en el reporte.
    val lat = state.reportLat
    val lng = state.reportLng
    if (lat != null && lng != null) {
        // recordamos la posición del marcador
        var markerPosition by remember { mutableStateOf(LatLng(lat, lng)) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(markerPosition, 12f)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color(0xFF3E8B75), RoundedCornerShape(16.dp))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    markerPosition = latLng
                    viewModel.changeLocation(latLng.latitude, latLng.longitude)
                }
            ) {
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = "Ubicación del reporte",
                    snippet = "Tocá en otro lugar para movel el marcador"
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Lat: %.5f, Lng: %.5f".format(markerPosition.latitude, markerPosition.longitude),
            color = Color.White,
            fontSize = 14.sp
        )
    } else {
        // Si no hay coordenadas, mostrmamos un mensaje opcional
        Text(
            text = "No hay ubicación registrada para este reporte.",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
