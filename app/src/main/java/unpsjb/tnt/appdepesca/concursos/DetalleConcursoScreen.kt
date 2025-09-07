package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

fun DetalleConcursoScreen(
    concursoId: Int,
    viewModel: ConcursosViewModel,
    navController: NavController
){
    Spacer(modifier = Modifier.height(16.dp))
    val concurso = viewModel.concursos.find { it.concursoId == concursoId }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
            .padding(16.dp),
    ) {

        //////CONCURSO
        Column {
            Text(
                text = "${concurso?.concursoNombre}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /////LUGAR
        Column {
            Text(
                text = "${concurso?.concursoLugar}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /////FECHA
        Column {
            Text(
                text = "${concurso?.concursoFecha}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ////DESCRIPCIÓN
        Column {
            Text(
                text = "${concurso?.concursoDescripcion}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        //////BASES
        Column {
            Text(
                text = "${concurso?.concursoBases}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E8B75),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //////PREMIO
        Column {
            Text(
                text = "${concurso?.concursoPremio}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        /////BOTÓN DE RETROCESO
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(88.dp) // fuerza cuadrado perfecto
                .offset(x = 8.dp, y = (-15).dp) // manejar corrimiento del botón
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
