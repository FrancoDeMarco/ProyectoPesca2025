package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.R
import java.nio.file.WatchEvent

@Composable
fun ListaConcursosScreen(
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    )
    {
        Column(
            modifier = Modifier
                .background(Color(0xFF1B2B24))
                .padding(horizontal = 16.dp)
        ){
            Spacer(modifier = Modifier.height(8.dp))
            TituloConcurso()
            Spacer(modifier = Modifier.height(8.dp))
            ListadoDeConcursos(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.weight(1f) // ocupa solo el espacio restante dentro de Column
            )
        }
        BotonRetroceso(navController,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 24.dp, y = (-32).dp)
        )
    }
}


/////BOTÓN DE RETROCESO
@Composable
fun BotonRetroceso(
    navController: NavController,
    modifier: Modifier){
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

//////////////TITULO/////////////////////
@Composable
fun TituloConcurso() {
    Text(
        text = "Concursos",
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

///////////////CONCURSOS////////////////////////////
@Composable
fun ListadoDeConcursos(
    viewModel: ConcursosViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(viewModel.concursos) { index, concurso ->
            ConcursoItem(concurso = concurso) {
                navController.navigate("detalleconcurso/${concurso.concursoId}")
            }
            if (index != viewModel.concursos.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
