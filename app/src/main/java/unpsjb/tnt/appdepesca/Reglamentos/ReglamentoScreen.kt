package unpsjb.tnt.appdepesca.Reglamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.database.Reglamento

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
            .background(Color(0xFF9EEE9E))
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Reglamento",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "Lugar",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h6
            )
        }
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp), color = Color.Gray
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.reglamentos) { reglamento ->
                ReglamentoItem(reglamento = reglamento) {
                    selectedReglamento.value = reglamento
                    showDialog.value = true
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Button(
            onClick = {
                navController.navigate("reportes")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFC3C3C))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
            Text("Volver")
        }
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