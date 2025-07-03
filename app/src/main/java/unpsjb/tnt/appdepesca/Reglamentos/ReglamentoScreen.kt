package unpsjb.tnt.appdepesca.Reglamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.database.Reglamento
import androidx.compose.material3.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.reporte.VolverButton


@Composable
fun ReglamentoScreen(
    viewModel: ReglamentosViewModel,
    navController: NavController
) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedReglamento = remember { mutableStateOf<Reglamento?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TituloReglamentos()
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = "Reglamento",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF3E8B75), // letra verde
                textAlign = TextAlign.Center
            )
            Text(
                text = "Lugar",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF3E8B75), // letra verde
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        Divider(
            color = Color(0xFF3E8B75),
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.reglamentos) { reglamento ->
                ReglamentoItem(reglamento = reglamento) {
                    selectedReglamento.value = reglamento
                    showDialog.value = true
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        ///////////VOLVER////////////////
        VolverButton(navController, showDialog)
    }

    if (showDialog.value && selectedReglamento.value != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                selectedReglamento.value = null
            },
            title = {
                Text(text = selectedReglamento.value?.reglamentoNombre ?: "")
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Lugar: ${selectedReglamento.value?.reglamentoLugar ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha de Vigencia: ${selectedReglamento.value?.relgamentoFecha ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Descripción: ${selectedReglamento.value?.reglamentoDescripcion ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Enlaces: ${selectedReglamento.value?.reglamentoEnlaces?.joinToString(", ") ?: ""}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        selectedReglamento.value = null
                    }
                ) {
                    Text(text = "Cerrar")
                }
            }
        )
    }
}

//////////////TITULO/////////////////////
@Composable
fun TituloReglamentos() {
    Text(
        text = "Reglamentos",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}