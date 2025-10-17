package unpsjb.tnt.appdepesca.reportes


import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import unpsjb.tnt.appdepesca.R
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.login.HeaderImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date
import kotlin.Boolean

@Composable
fun ListadoReportesScreen(
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
) {
    val reportes = listadoReportesViewModel.state.report
    val showDialog = remember { mutableStateOf(false) }
    val reportToDelete = remember { mutableStateOf<Reporte?>(null) }
    val selectedReport = remember { mutableStateOf<Reporte?>(null) }
    val context = LocalContext.current                                      //Variables para filtrar por fecha
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val fromDate = remember { mutableStateOf<Date?>(null) }
    val toDate = remember { mutableStateOf<Date?>(null) }
    val dateButtonModifier = Modifier //variable para darle groso, color y forma al borde de los bootones
        .border(
            width = 2.dp,               // grosor
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // forma
        )
    val dateButtonColors = ButtonDefaults.buttonColors( // variable que le da color al interior del botón
        containerColor = Color(0xFF1B2B24)
    )

    LaunchedEffect(Unit) {
        fromDate.value = null
        toDate.value = null
        listadoReportesViewModel.setFechasFiltro(null, null)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HeaderImage(size = 100.dp)
                }
                TituloReportes()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Desde(context, fromDate, toDate, listadoReportesViewModel, dateButtonModifier,  dateButtonColors,  dateFormatter)
                    Hasta(context, fromDate, toDate, listadoReportesViewModel, dateButtonModifier,  dateButtonColors,  dateFormatter)
                    Refrescar(fromDate, toDate, listadoReportesViewModel, dateButtonModifier,  dateButtonColors)
                }
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Cabecera()
                }
                LineaDivisoria()
                Button(onClick = { navController.navigate("mapa_reportes")}){
                    Text("Ver en mapa")
                }
            }
            ListaReportes(listadoReportesViewModel, reportes, navController, reportToDelete, showDialog)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonModifier = Modifier
                .weight(1f)                 // Ocupa 1/4 del ancho disponible
                .aspectRatio(1f)            // Hace que el alto sea igual al ancho (cuadrado)
                .padding(horizontal = 4.dp) // Espacio entre botones
            Agregar(navController, buttonModifier)
            Concursos(navController, buttonModifier)
            Reglamentos(navController, buttonModifier)
            Salir(navController, buttonModifier)
        }
    }
    DetallesReporte(selectedReport)
    EliminarReporte(showDialog, reportToDelete, listadoReportesViewModel)
}

////////////////BOTONES QUE MUESTRAN LAS FECHAS A FILTRAR/////////////////////
fun showDatePicker(
    context: Context,
    initialDate: Date? = null,
    minDate: Date? = null,  // <- nueva opción
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    initialDate?.let {  // Si hay una fecha seleccionada, la usamos como inicial
        calendar.time = it
    }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(selectedCalendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    minDate?.let {  // Aplicar fecha mínima si está definida
        datePickerDialog.datePicker.minDate = it.time
    }
    datePickerDialog.show()
}

//////////////TITULO/////////////////////
@Composable
fun TituloReportes() {
    Text(
        text = "Reportes",
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}

//////////////DESDE/////////////////////
@Composable
fun Desde(
    context: Context,
    fromDate: MutableState<Date?>,
    toDate: MutableState<Date?>,
    listadoReportesViewModel: ListadoReportesViewModel,
    dateButtonModifier: Modifier,
    dateButtonColors: ButtonColors,
    dateFormatter: SimpleDateFormat
){
    Button(
        onClick = {
            showDatePicker(
                context = context,
                initialDate = fromDate.value
            ) { selectedDate ->
                fromDate.value = selectedDate
                toDate.value = null //Borra la fecha Hasta
                listadoReportesViewModel.setFechasFiltro(fromDate.value, null)
            }
        },
        modifier = dateButtonModifier,
        colors = dateButtonColors,
        shape = RectangleShape  // forma
    ) {
        Text(
            text = "Desde: ${fromDate.value?.let { dateFormatter.format(it) } ?: "---"}",
            color = Color.White
        )
    }
}


//////////////HASTA/////////////////////
@Composable
fun Hasta(
    context: Context,
    fromDate: MutableState<Date?>,
    toDate: MutableState<Date?>,
    listadoReportesViewModel: ListadoReportesViewModel,
    dateButtonModifier: Modifier,
    dateButtonColors: ButtonColors,
    dateFormatter: SimpleDateFormat
){
    if (fromDate.value != null) { //deshabilitado si no hay fecha "Desde"
        Button(
            onClick = {
                showDatePicker(
                    context = context,
                    initialDate = toDate.value,          // mostrar la última fecha
                    minDate = fromDate.value // no deja elegir fechas anteriores a "Desde"
                ) { selectedDate ->
                    toDate.value = selectedDate
                    listadoReportesViewModel.setFechasFiltro(
                        fromDate.value,
                        toDate.value
                    )
                }
            },
            modifier = dateButtonModifier,
            colors = dateButtonColors,
            shape = RectangleShape, // forma
        ) {
            Text(
                text = "Hasta: ${toDate.value?.let { dateFormatter.format(it) } ?: "---"}",
                color = Color.White
            )
        }
    }
}

//////////////REFRESCAR/////////////////////
@Composable
fun Refrescar(
    fromDate: MutableState<Date?>,
    toDate: MutableState<Date?>,
    listadoReportesViewModel: ListadoReportesViewModel,
    dateButtonModifier: Modifier,
    dateButtonColors: ButtonColors,
){
    if (fromDate.value != null) { //deshabilitado si no hay fecha "Desde"
        Button(
            onClick = {
                fromDate.value = null
                toDate.value = null
                listadoReportesViewModel.setFechasFiltro(null, null)
            },
            modifier = dateButtonModifier,
            colors = dateButtonColors,
            shape = RectangleShape, // forma
        ) {
            Text("Refrescar")
        }
    }
}

//////////////CABECERA/////////////////////
@Composable
fun RowScope.Cabecera(){
    Text(
        text = "Titulo",
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF3E8B75) // letra verde
    )
    Text(
        text = "Fecha",
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF3E8B75), // letra verde
        textAlign = TextAlign.Center
    )
    Text(
        text = "Acciones",
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.titleLarge,
        color = Color(0xFF3E8B75), // letra verde
        textAlign = TextAlign.End
    )
}

//////////////REPORTES/////////////////////
fun LazyListScope.ListaReportes(
    listadoReportesViewModel: ListadoReportesViewModel,
    reportes: List<Reporte>?,
    navController: NavController,
    reportToDelete: MutableState<Reporte?>,
    showDialog: MutableState<Boolean>
){
    reportes?.let { list -> //Listado de Reportes
        items(list) { reporte ->
            ItemReporte(
                reporte = reporte,
                listadoReportesViewModel = listadoReportesViewModel,
                modifier = Modifier.fillMaxWidth(),
                onEdit = {
                    listadoReportesViewModel.loadReport(reporte) // precarga los datos del reporte
                    navController.navigate("editar_reporte/${reporte.reportId}")
                },

                onDelete = { //Eliminar Reporte
                    reportToDelete.value = reporte
                    showDialog.value = true
                },
                onItemClick = {
                    navController.navigate("detalle_reporte/${reporte.reportId}")
                }
            )
        }
    }
}

//////////////BOTÓN AGREGAR/////////////////////
@Composable
fun Agregar(
    navController: NavController,
    buttonModifier: Modifier
){
    Button(
        onClick = { navController.navigate("formulario") },
        modifier = buttonModifier.border(
            width = 2.dp,               // grosor del borde
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // importante: que coincida con el shape del botón
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
        shape = RectangleShape  //esto lo hace cuadrado
    ) {
        Text(
            text = "+",
            fontSize = 30.sp    // ajustás el tamaño acá
        )
    }
}

@Composable
fun Concursos(
    navController: NavController,
    buttonModifier: Modifier
){
    Button(
        onClick = { navController.navigate("concurso") },
        modifier = buttonModifier.border(
            width = 2.dp,               // grosor del borde
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // importante: que coincida con el shape del botón
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
        shape = RectangleShape  // <- esto lo hace cuadrado
    ) {
        Image(
            painter = painterResource(R.drawable.concursos),
            contentDescription = "Concursos",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun Reglamentos(
    navController: NavController,
    buttonModifier: Modifier
){
    Button(
        onClick = { navController.navigate("reglamentos") },
        modifier = buttonModifier.border(
            width = 2.dp,               // grosor del borde
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // importante: que coincida con el shape del botón
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
        shape = RectangleShape  // esto lo hace cuadrado
    ) {
        Image(
            painter = painterResource(R.drawable.reglamento),
            contentDescription = "Reglamento",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun Salir(
    navController: NavController,
    buttonModifier: Modifier
){
    Button(
        onClick = { navController.navigate("login") },
        modifier = buttonModifier.border(
            width = 2.dp,               // grosor del borde
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // importante: que coincida con el shape del botón
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
        shape = RectangleShape // <- esto lo hace cuadrado
    ) {
        Image(
            painter = painterResource(R.drawable.salir), //nombre de la imagen
            contentDescription = "Salir",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun DetallesReporte(
    selectedReport: MutableState<Reporte?>
){
    if (selectedReport.value != null) {
        AlertDialog(
            onDismissRequest = {
                selectedReport.value = null
            },
            title = {
                Text(text = selectedReport.value?.reportTitulo ?: "")
            },
            text = {
                Column {
                    Text(text = "Descripción: ${selectedReport.value?.reportDescripcion ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha: ${selectedReport.value?.reportFecha ?: ""}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedReport.value = null
                    }
                ) {
                    Text(text = "Cerrar")
                }
            }
        )
    }
}

@Composable
fun EliminarReporte(
    showDialog: MutableState<Boolean>,
    reportToDelete: MutableState<Reporte?>,
    listadoReportesViewModel: ListadoReportesViewModel
){
    if (showDialog.value) {
        ConfirmationDialog(
            onConfirm = {
                reportToDelete.value?.let { listadoReportesViewModel.deleteReporte(it) }
                showDialog.value = false
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text("¿Está seguro que desea eliminar este reporte?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
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

@Composable
fun LineaDivisoria(){
    Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
    Divider(
        color = Color(0xFF3E8B75),
        thickness = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
}
