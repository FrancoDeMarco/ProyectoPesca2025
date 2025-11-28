package unpsjb.tnt.appdepesca.reglamentos

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Reglamento
import androidx.compose.runtime.getValue

@Composable
fun ReglamentoItem(reglamento: Reglamento, onItemSelected: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 6.dp)
            .clickAnimation(onClick = onItemSelected),
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

@Composable
fun Modifier.clickAnimation(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "scaleAnim"
    )
    return this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}
