package unpsjb.tnt.appdepesca.Reglamentos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.R


@Composable
fun ListaReglamentosScreen(
    viewModel: ReglamentosViewModel,
    navController: NavController
) {
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
                    navController.navigate("detalleReglamento/${reglamento.reglamentoId}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(88.dp) // fuerza cuadrado perfecto
                .offset(x = 24.dp, y = (-32).dp) // manejar corrimiento del botón
                .border(2.dp, Color(0xFF3E8B75), RectangleShape),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
            shape = RectangleShape, // <- esto lo hace cuadrado
            contentPadding = PaddingValues(0.dp) // quita el padding interno por defecto
        ) {
            Image(
                painter = painterResource(R.drawable.retroceso), //nombre de la imagen
                contentDescription = "Retroceso",
                modifier = Modifier.size(50.dp) // tamaño de la imagen dentro del botón
            )
        }
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