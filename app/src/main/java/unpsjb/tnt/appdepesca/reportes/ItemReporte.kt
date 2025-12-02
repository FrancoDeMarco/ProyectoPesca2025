package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Reporte
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color


@Composable
fun ItemReporte(
    reporte: Reporte,
    modifier: Modifier = Modifier,
    onEdit: (Reporte) -> Unit,
    onDelete: () -> Unit,
    onItemClick: (Reporte) -> Unit // Agregado: Función para mostrar detalles del reporte
) {
    Row(modifier = modifier.clickable { onItemClick(reporte) }) { // Modificado: Agregado el listener de click
        Text(
            text = reporte.reportTitulo,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF3E8B75) // letra verde
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = reporte.reportFecha,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF3E8B75) // letra verde
        )
        IconButton(onClick = { onEdit(reporte) }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF3E8B75) // icono verde
            )
        }
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFF3E8B75) // icono verde
            )
        }
    }
}