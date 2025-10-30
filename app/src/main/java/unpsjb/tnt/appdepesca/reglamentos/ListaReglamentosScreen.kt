package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.reportes.BotonVolver


@Composable
fun ListaReglamentosScreen(
    viewModel: ReglamentosViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF1B2B24))
                .statusBarsPadding()
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
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 2.dp,
                color = Color(0xFF3E8B75)
            )
            Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(viewModel.reglamentos) { reglamento ->
                    ReglamentoItem(reglamento = reglamento) {
                        navController.navigate("detalleReglamento/${reglamento.reglamentoId}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        BotonVolver(navController)
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