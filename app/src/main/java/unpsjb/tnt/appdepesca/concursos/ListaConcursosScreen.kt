package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.reportes.BotonVolver

@Composable
fun ListaConcursosScreen(
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFF1B2B24))
            .statusBarsPadding()
            .fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .background(Color(0xFF1B2B24))
                .statusBarsPadding()
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
        BotonVolver(navController)
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
