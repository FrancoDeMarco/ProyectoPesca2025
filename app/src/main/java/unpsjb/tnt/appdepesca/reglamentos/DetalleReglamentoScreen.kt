package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
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

    Spacer(modifier = Modifier.height(16.dp))

    val reglamento = viewModel.reglamentos.find { it.reglamentoId == reglamentoId }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B2B24))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ///// REGLAMENTO
            Text(
                text = "${reglamento?.reglamentoNombre}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            ///// LUGAR
            Text(
                "${reglamento?.reglamentoLugar}",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            /////FECHA
            Text(
                text = "${reglamento?.relgamentoFecha}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75),
            )

            Spacer(modifier = Modifier.height(16.dp))

            ////DESCRIPCIÓN
            Column {
                Text(
                    text = "${reglamento?.reglamentoDescripcion}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ////ENLACES
            Column {
                reglamento?.reglamentoEnlaces?.forEach { enlace ->
                    Text(
                        text = enlace,
                        color = Color(0xFF3E8B75),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        BotonVolver(
            navController,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 24.dp, y = (-32).dp)
        )
    }
}

/////BOTÓN DE RETROCESO
@Composable
fun BotonVolver(
    navController: NavController,
    modifier: Modifier = Modifier) {
    Button(
        onClick = { navController.popBackStack() },
        modifier = modifier
            .size(88.dp) // fuerza cuadrado perfecto
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