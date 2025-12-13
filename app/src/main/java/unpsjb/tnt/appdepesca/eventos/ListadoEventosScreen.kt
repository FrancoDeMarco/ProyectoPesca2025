package unpsjb.tnt.appdepesca.eventos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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

            FiltrosEventos(
                categoriaActual = categoria,
                onCategoriaClick = { listadoEventosViewModel.filtrarPorCategoria(it) }
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
fun FiltrosEventos(
    categoriaActual: String,
    onCategoriaClick: (String) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        listOf(
            "todos" to "Todos",
            "concurso" to "Torneos",
            "reglamento" to "Reglamentos",
            "sorteo" to "Sorteos",
            "aviso" to "Avisos"
        ).forEach { (clave, texto) ->
            FilterChip(
                selected = categoriaActual == clave,
                onClick = { onCategoriaClick(clave) },
                label = { Text(texto) }
            )
        }
    }


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
