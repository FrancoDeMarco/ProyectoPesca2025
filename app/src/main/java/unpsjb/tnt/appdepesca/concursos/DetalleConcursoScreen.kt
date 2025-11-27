package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
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

@Composable
fun DetalleConcursoScreen(
    concursoId: Int,
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    val concurso = viewModel.concursos.find { it.concursoId == concursoId }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            //////TÍTULO
            item {
                Text(
                    text = "${concurso?.concursoNombre}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E8B75),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            /////CARD LUGAR
            item {
                InfoCard(title = "Lugar", text = concurso?.concursoLugar)
            }
            /////CARD FECHA
            item {
                InfoCard(title = "Fecha", text = concurso?.concursoFecha)
            }
            /////CARD DESCRIPCIÓN
            item {
                InfoCard(title = "Descripción", text = concurso?.concursoDescripcion)
            }
            /////CARD BASES
            item {
                InfoCard(title = "Bases", text = concurso?.concursoBases)
            }
            /////CARD PREMIO
            item {
                InfoCard(title = "Premio", text = concurso?.concursoPremio)
            }
        }
        BotonVolver(navController)
    }
}

@Composable
fun InfoCard(title: String, text: String?){
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFF223029)
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Text(
                text = title,
                color = Color(0xFF3E8B75),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text ?: "",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
