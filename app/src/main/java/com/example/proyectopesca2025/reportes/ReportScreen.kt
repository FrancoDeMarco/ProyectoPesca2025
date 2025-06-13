package com.example.proyectopesca2025.reportes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectopesca2025.database.Reporte
import com.example.proyectopesca2025.formulario.FormularioViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
fun ReportScreen(
    reportViewModel: ReportViewModel,
    formularioViewModel: FormularioViewModel,
    navController: NavController
) {
    val fromDate = remember { mutableStateOf<Date?>(null) }
    val toDate = remember { mutableStateOf<Date?>(null) }

    val reportes by reportViewModel.getAllReportesFlow().collectAsState(null)

    val showDialog = remember { mutableStateOf(false) }
    val reportToDelete = remember { mutableStateOf<Reporte?>(null) }

    val selectedReport = remember { mutableStateOf<Reporte?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF9EEE9E))
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Titulo",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    DatePickerComponent(
                        fromDate = fromDate.value,
                        onFromDateSelected = { fromDate.value = it },
                        toDate = toDate.value,
                        onToDateSelected = { toDate.value = it },
                        onSearch = {
                            reportViewModel.setFechasFiltro(fromDate.value, toDate.value)
                        }
                    )
                }
                Divider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            reportes?.let { list ->
                items(list) { reporte ->
                    ItemReporte(
                        reporte = reporte,
                        reportViewModel = reportViewModel,
                        modifier = Modifier.fillMaxWidth(),
                        onEdit = {
                            Log.i("HomeScreen", "editar")
                        },
                        onDelete = {
                            reportToDelete.value = reporte
                            showDialog.value = true
                        },
                        onItemClick = {
                            selectedReport.value = it
                        }
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("concurso") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C8DFC))
            ) {
                Text(text = "Concursos")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("reglamentos")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE140FD))
            ) {
                Text(text = "Reglamentos")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("formulario")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF59FC3C))
            ) {
                Text(text = "Agregar Reporte")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFC3C3C))
            ) {
                Text("Cerrar Sesión")
            }
        }
    }

    if (showDialog.value) {
        ConfirmationDialog(
            onConfirm = {
                reportToDelete.value?.let { reportViewModel.deleteReporte(it) }
                showDialog.value = false
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }

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




/*****Filtro de fechas******/
@Composable
fun DatePickerComponent(
    fromDate: Date?,
    onFromDateSelected: (Date?) -> Unit,
    toDate: Date?,
    onToDateSelected: (Date?) -> Unit,
    onSearch: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fromDateText = remember { mutableStateOf(fromDate?.let { dateFormatter.format(it) } ?: "") }
    val toDateText = remember { mutableStateOf(toDate?.let { dateFormatter.format(it) } ?: "") }

    val isFromDateValid = fromDateText.value.isDateFormatValid()
    val isToDateValid = toDateText.value.isDateFormatValid()
    val isDatesValid = isFromDateValid && isToDateValid && areDatesRealistic(fromDate, toDate) && areDaysValid(fromDateText.value) && areDaysValid(toDateText.value) && isToDateGreaterThanOrEqualFromDate(fromDateText.value, toDateText.value)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = fromDateText.value,
            onValueChange = { newText ->
                fromDateText.value = newText
                val parsedDate = try {
                    dateFormatter.parse(newText)
                } catch (e: ParseException) {
                    null
                }
                onFromDateSelected(parsedDate)
            },
            placeholder = { Text(text = "dd/mm/aaaa") },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)

        )
        Spacer(modifier = Modifier.width(16.dp))
        TextField(
            value = toDateText.value,
            onValueChange = { newText ->
                toDateText.value = newText
                val parsedDate = try {
                    dateFormatter.parse(newText)
                } catch (e: ParseException) {
                    null
                }
                onToDateSelected(parsedDate)
            },
            placeholder = { Text(text = "dd/mm/aaaa") },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { onSearch() },
            modifier = Modifier
                .padding(end = 16.dp),
            enabled = isDatesValid
        ) {
            Text(text = "Buscar")
        }
    }
}



private fun String.isDateFormatValid(): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        val date = dateFormat.parse(this)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        year in 1900..2100 && month in 1..12 && day in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    } catch (e: ParseException) {
        false
    }
}

private fun areDatesRealistic(fromDate: Date?, toDate: Date?): Boolean {
    if (fromDate == null || toDate == null) {
        return true
    }
    return fromDate <= toDate
}

private fun areDaysValid(dateText: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        val date = dateFormat.parse(dateText)
        true
    } catch (e: ParseException) {
        false
    }
}

private fun isToDateGreaterThanOrEqualFromDate(fromDateText: String, toDateText: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        val fromDate = dateFormat.parse(fromDateText)
        val toDate = dateFormat.parse(toDateText)
        toDate >= fromDate
    } catch (e: ParseException) {
        false
    }
}