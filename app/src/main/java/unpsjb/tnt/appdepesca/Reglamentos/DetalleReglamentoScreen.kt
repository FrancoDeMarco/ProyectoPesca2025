package unpsjb.tnt.appdepesca.Reglamentos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.R

@Composable
fun DetalleReglamentoScreen(
    reglamentoId: Int,
    viewModel: ReglamentosViewModel,
    navController: NavController
) {
    val reglamento = viewModel.reglamentos.find { it.reglamentoId == reglamentoId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        ///// REGLAMENTO
        Text(
            text = "${reglamento?.reglamentoNombre}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75),
        )

        ///// LUGAR
        Text(
            text = "Lugar: ${reglamento?.reglamentoLugar}",
            fontSize = 18.sp,
            color = Color.White
        )

        /////FECHA
        Text(
            text = "Fecha de Vigencia: ${reglamento?.relgamentoFecha}",
            fontSize = 18.sp,
            color = Color.White
        )

        ////DESCRIPCIÓN
        Column {
            Text(
                text = "Descripción:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75)
            )
            Text(
                text = "${reglamento?.reglamentoDescripcion}",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        ////ENLACES
        Column {
            Text(
                text = "Enlaces:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF3E8B75)
            )
            reglamento?.reglamentoEnlaces?.forEach { enlace ->
                Text(
                    text = enlace,
                    color = Color.White,
                    fontSize = 18.sp,
                )
            }
        }

        /////BOTÓN DE RETROCESO
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