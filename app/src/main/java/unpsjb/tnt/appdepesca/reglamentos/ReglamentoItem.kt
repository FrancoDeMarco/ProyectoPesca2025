package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Reglamento

@Composable
fun ReglamentoItem(reglamento: Reglamento, onItemSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = reglamento.reglamentoNombre,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF3E8B75) // letra verde
        )
        Text(
            text = reglamento.reglamentoLugar,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF3E8B75) // letra verde
        )
    }
}