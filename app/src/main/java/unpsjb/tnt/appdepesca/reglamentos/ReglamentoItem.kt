package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Reglamento

@Composable
fun ReglamentoItem(reglamento: Reglamento, onItemSelected: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 6.dp)
            .clickable { onItemSelected() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF223029)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            Text(
                text = reglamento.reglamentoNombre,
                color = Color(0xFF3E8B75), // letra verde
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reglamento.reglamentoLugar,
                color = Color.White.copy(alpha = 0.8f), // letra verde
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = reglamento.reglamentoFecha,
                color = Color(0xFF3E8B75), // letra verde
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
