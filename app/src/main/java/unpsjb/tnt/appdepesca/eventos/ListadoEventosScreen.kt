package unpsjb.tnt.appdepesca.eventos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.reportes.BotonVolver
import unpsjb.tnt.appdepesca.reglamentos.clickAnimation
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ListadoEventosScreen(
    navController: NavController,
    listadoEventosViewModel: ListadoEventosViewModel
) {
    val eventos by listadoEventosViewModel.eventos.collectAsState()
    val categoria by listadoEventosViewModel.categoriaSeleccionada.collectAsState()
    val filtrados = when(categoria){
        "todos" -> eventos
        else -> eventos.filter {it.categoria == categoria}
    }

    Box(
        modifier = Modifier
            .background(color = Color(0xFF1B2B24))
            .fillMaxSize()
    ){
        Column(Modifier.padding(16.dp)){
            TituloSeccion("Eventos")
            FiltroEventosDropdown(//Esta función se encarga de filtrar los eventos por categoría y actualizar la lista filtrada.
                categoriaActual = categoria,
                onCategoriaSeleccionada = {
                    listadoEventosViewModel.filtrarPorCategoria(it)
                }
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filtrados){ evento ->
                    EventoItem(evento) { id ->
                        navController.navigate("detalleEvento/$id")
                    }
                }
            }
        }
        BotonVolver(navController)
    }
}

@Composable
fun TituloSeccion(text: String) {
    Text(
        text = text,
        color = Color(0xFF3E8B75),
        fontSize = 30.sp,
    )
}

@Composable
fun EventoItem(evento: Evento, onClick: (String) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickAnimation{onClick(evento.id)},
        colors = CardDefaults.cardColors(containerColor = Color(0xFF223029))
    ){
        Column(Modifier.padding(16.dp)){
            Text(text = evento.titulo, color = Color(0xFF3E8B75), fontSize = 20.sp)
            evento.lugar?.let{
                Text(it, color = Color.White.copy(0.8f))
            }
            evento.fecha?.let {
                Text(it, color = Color(0xFF3E8B75))
            }

        }
    }
}

// ======== FILTRO DE EVENTOS ========
@OptIn (ExperimentalMaterial3Api::class)
@Composable
fun FiltroEventosDropdown(
    categoriaActual: String,
    onCategoriaSeleccionada: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    val categorias = listOf(
        "todos" to "Todos",
        "concurso" to "Torneos",
        "reglamento" to "Reglamentos",
        "sorteo" to "Sorteos",
        "aviso" to "Aviso"
    )

    val textoSeleccionado =
        categorias.firstOrNull { it.first == categoriaActual }?.second ?: "Todos"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        TextField(
            value = textoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de evento") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            categorias.forEach { (clave, texto) ->
                DropdownMenuItem(
                    text = { Text(texto) },
                    onClick = {
                        onCategoriaSeleccionada(clave)
                        expanded = false
                    }
                )
            }
        }
    }
}
