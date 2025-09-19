package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import unpsjb.tnt.appdepesca.reglamentos.BotonVolver

@Composable
fun DetalleConcursoScreen(
    concursoId: Int,
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(16.dp))
    val concurso = viewModel.concursos.find { it.concursoId == concursoId }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B2B24))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //////CONCURSO
            Column {
                Text(
                    text = "${concurso?.concursoNombre}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E8B75),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            /////LUGAR
            Column {
                Text(
                    text = "${concurso?.concursoLugar}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            /////FECHA
            Column {
                Text(
                    text = "${concurso?.concursoFecha}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E8B75),
                )
            }
            ////DESCRIPCIÓN
            Column {
                Text(
                    text = "${concurso?.concursoDescripcion}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
            //////BASES
            Column {
                Text(
                    text = "${concurso?.concursoBases}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E8B75),
                )
            }
            //////PREMIO
            Column {
                Text(
                    text = "${concurso?.concursoPremio}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
        BotonVolver(
            navController,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 24.dp, y = (-32).dp)
        )
    }
}


