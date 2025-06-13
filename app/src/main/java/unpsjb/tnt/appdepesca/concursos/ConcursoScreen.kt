package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.database.Concurso

@Composable
fun ConcursoScreen(
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedConcurso = remember { mutableStateOf<Concurso?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9EEE9E))
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Concurso",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, start = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Lugar",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, start = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = Color.Gray
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(viewModel.concursos) { index, concurso ->
                ConcursoItem(concurso = concurso) {
                    selectedConcurso.value = concurso
                    showDialog.value = true
                }
                if (index != viewModel.concursos.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Button(
            onClick = {
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
    }

    if (showDialog.value && selectedConcurso.value != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                selectedConcurso.value = null
            },
            title = {
                Text(text = selectedConcurso.value?.concursoNombre ?: "")
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Lugar: ${selectedConcurso.value?.concursoLugar ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha: ${selectedConcurso.value?.concursoFecha ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Descripción: ${selectedConcurso.value?.concursoDescripcion ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Bases: ${selectedConcurso.value?.concursoBases ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Premio: ${selectedConcurso.value?.concursoPremio ?: ""}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        selectedConcurso.value = null
                    }
                ) {
                    Text(text = "Cerrar")
                }
            }
        )
    }
}


