package unpsjb.tnt.appdepesca.eventos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.reportes.BotonVolver
import unpsjb.tnt.appdepesca.ui.components.InfoCard

@Composable
fun DetalleEventoScreen(
    eventoId: String,
    listadoEventosViewModel: ListadoEventosViewModel,
    navController: NavController
){
    val evento = listadoEventosViewModel.eventos
        .collectAsState()
        .value
        .find { it.id == eventoId }
    Box(
        modifier = Modifier
            .background(Color(0xFF1B2B24))
            .fillMaxSize()
    ){
        // Si el evento no cargó, muestra un mensaje
        if (evento == null){
            Text(
                text = "Cargando evento...",
                color = Color.White,
                fontSize = 24.sp
            )
        }
        // Esto solo cuando el evento existe
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                Text(
                    text = evento?.titulo ?: "",
                    fontSize = 28.sp,
                    color = Color(0XFF3E8B75),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }
            evento?.lugar
                ?.takeIf { it.isNotBlank()} // Elimina el campo si está vacío
                ?.let { item { InfoCard("Lugar", it) } }
            evento?.fecha
                ?.takeIf { it.isNotBlank()}
                ?.let { item { InfoCard("Fecha", it) } }
            evento?.descripcion
                ?.takeIf { it.isNotBlank()}
                ?.let { item { InfoCard("Descripcion", it) } }
            evento?.bases
                ?.takeIf { it.isNotBlank()}
                ?.let {  item { InfoCard("Bases", it) } }
            evento?.premio
                ?.takeIf { it.isNotBlank()}
                ?.let {  item { InfoCard("Premio", it) } }
            evento?.enlaces
                ?.takeIf { it.isNotEmpty()}
                ?.let { item { InfoCard( "Enlaces", it.joinToString("\n") { enlace -> "• $enlace" }) } } }
        BotonVolver(navController)
    }
}
