package unpsjb.tnt.appdepesca.Reglamentos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Text(
            text = reglamento.reglamentoLugar,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}