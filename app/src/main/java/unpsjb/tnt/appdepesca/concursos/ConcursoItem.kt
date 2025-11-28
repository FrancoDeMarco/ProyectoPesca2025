package unpsjb.tnt.appdepesca.concursos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Concurso
import unpsjb.tnt.appdepesca.reglamentos.clickAnimation

@Composable
fun ConcursoItem(concurso: Concurso, onItemClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickAnimation(onClick = onItemClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF223029)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)){
            // Nombre del concurso
            Text(
                text = concurso.concursoNombre,
                color = Color(0xFF3E8B75), // Color deseado
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            //Lugar
            Text(
                text = concurso.concursoLugar,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
            //Fecha
            Text(
                text = concurso.concursoFecha,
                color = Color(0xFF3E8B75),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
