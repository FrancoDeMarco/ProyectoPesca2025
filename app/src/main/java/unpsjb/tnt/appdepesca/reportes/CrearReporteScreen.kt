package unpsjb.tnt.appdepesca.reportes


import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import unpsjb.tnt.appdepesca.login.HeaderImage
import java.util.Calendar
import kotlin.Boolean
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun CrearReporteScreen(
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
) {
    val state = listadoReportesViewModel.state
    val dateState = remember { mutableStateOf(TextFieldValue("")) }
    val isTitleValido = remember { mutableStateOf(false) }
    var isDescriptionValido = remember { mutableStateOf(false) }
    var isDateValido = remember { mutableStateOf(false) }
    var isImagenValido = remember { mutableStateOf(false) }
    val formValido by remember(state) {
        derivedStateOf {
            state.reportDate.isNotBlank() &&
                    state.reportTitle.isNotBlank() &&
                    state.reportDescription.isNotBlank() &&
                    !state.reportImagenUri.isNullOrBlank()
        }
    }
    LaunchedEffect(Unit) {
        isTitleValido.value = listadoReportesViewModel.state.reportTitle.isNotEmpty()
        isDescriptionValido.value = listadoReportesViewModel.state.reportDescription.isNotEmpty()
        isDateValido.value = listadoReportesViewModel.state.reportDate.isNotEmpty()
        isImagenValido.value = listadoReportesViewModel.state.reportImagenUri?.isNotEmpty() == true
    }
    LaunchedEffect(state.reportDate) {
        dateState.value = TextFieldValue(state.reportDate)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1B2B24))
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderImage(size = 200.dp) // usa un tamaño personalizado
            TituloReporte()
            NombreReporte(listadoReportesViewModel, state, isTitleValido)
            DescripcionReporte(listadoReportesViewModel, state, isDescriptionValido)
            FechaReporte(listadoReportesViewModel, dateState, isDateValido)
            AgregarFotoButton(viewModel = listadoReportesViewModel, isImagenValido)
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 24.dp)
            ){
                BotonVolverCrear(navController, listadoReportesViewModel)
            }
            SiguienteCrear(
                navController = navController,
                enabled = formValido,
                modifier = Modifier)
        }
    }
}

@Composable
fun BotonVolverCrear(
    navController: NavController,
    listadoReportesViewModel: ListadoReportesViewModel
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = {
                listadoReportesViewModel.clearForm()
                navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(64.dp)
                .background(
                    color = Color.White.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retroceso",
                tint = Color(0xFF3E8B75) // color del ícono
            )
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

//////////////BOTÓN AGREGAR REPORTE//////
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
        Text("Agregar Reporte")
    }
}

//////////////NOMBRE DEL REPORTE/////////
@Composable
fun NombreReporte(
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
fun DescripcionReporte(
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
fun FechaReporte(
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

@Composable
fun SiguienteCrear(
    navController: NavController,
    enabled: Boolean,
    modifier: Modifier
){
    Button(
        onClick = { navController.navigate("seleccionar_ubicacion_crear") },
        enabled = enabled,
        modifier = modifier
            .border(
                width = 2.dp,               // grosor del borde
                //color = Color(0xFF3E8B75),  // color del borde
                color = if (enabled) Color(0xFF3E8B75) else Color.Gray,
                shape = RectangleShape      // importante: que coincida con el shape del botón
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color(0xFF1B2B24) else Color (0xFF2E2E2E),
            disabledContainerColor = Color(0xFF2E2E2E),
            disabledContentColor = Color.Gray
        ),
        shape = RectangleShape  //esto lo hace cuadrado
    ) {
        Text(
            text = "Siguiente",
            fontSize = 30.sp // tamaño
        )
    }
}

@Composable
fun AgregarFotoButton(
    viewModel: ListadoReportesViewModel,
    isImagenValido: MutableState<Boolean>
){
    val uri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { selectedUri ->
        selectedUri?.let {
            uri.value = it
            viewModel.changeImage(it)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E8B75))
        ) {
            Icon(Icons.Default.Photo, contentDescription = "Seleccionar imagen")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Seleccionar Imagen")
        }
        uri.value?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen del reporte",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            isImagenValido.value = viewModel.state.reportImagenUri != null//Con esto supuestamente ya no se pierde la imagen al volver a atrás
        }
    }
}
