package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.reportes.BotonVolver
import unpsjb.tnt.appdepesca.ui.components.InfoCard

@Composable
fun DetalleReglamentoScreen(
    reglamentoId: Int,
    viewModel: ReglamentosViewModel,
    navController: NavController
) {
    val reglamento = viewModel.reglamentos.find { it.reglamentoId == reglamentoId }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ///// TÍTULO
           item{
               Text(
                   text = reglamento?.reglamentoNombre ?: "",
                   fontSize = 28.sp,
                   fontWeight = FontWeight.Bold,
                   color = Color(0xFF3E8B75),
                   textAlign = TextAlign.Center,
                   modifier = Modifier.fillMaxWidth()
               )
               Spacer(modifier = Modifier.height(16.dp))
           }
            ///// LUGAR
            item{
                InfoCard(title = "Lugar", text = reglamento?.reglamentoLugar)
            }
            ///// FECHA
            item{
                InfoCard(title = "Fecha", text = reglamento?.reglamentoFecha)
            }
            ///// DESCRIPCIÓN
            item{
                InfoCard(title = "Descripción", text = reglamento?.reglamentoDescripcion)
            }
            ///// ENLACES
            item{
                InfoCard(title = "Enlaces", text = reglamento?.reglamentoEnlaces?.joinToString("\n") {"• $it"}?:"")
            }
        }
        BotonVolver(navController)
    }
}
