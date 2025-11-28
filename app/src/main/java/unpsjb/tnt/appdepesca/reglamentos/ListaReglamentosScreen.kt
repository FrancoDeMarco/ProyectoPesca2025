package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TituloReglamentos()

            LazyColumn (modifier = Modifier.weight(1f)) {
                items(viewModel.reglamentos) { reglamento ->
                    ReglamentoItem(reglamento) {
                        navController.navigate("detalleReglamento/${reglamento.reglamentoId}")
                    }
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