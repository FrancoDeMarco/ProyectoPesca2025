package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.database.Concurso
import unpsjb.tnt.appdepesca.reporte.VolverButton
import kotlin.Boolean

@Composable
fun ListaConcursosScreen(
    viewModel: ConcursosViewModel,
    navController: NavController
) {
    val showDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TituloConcurso()
        Spacer(modifier = Modifier.height(8.dp))
        ListadoDeConcursos(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier.weight(1f) // ocupa solo el espacio restante dentro de Column
        )
        Spacer(modifier = Modifier.height(8.dp))
        VolverButton(navController, showDialog)
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
        modifier = Modifier.padding(bottom = 16.dp)
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
