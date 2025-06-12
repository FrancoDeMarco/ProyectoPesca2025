package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import unpsjb.tnt.appdepesca.database.Reporte
import androidx.compose.material.Text


@Composable
fun ItemReporte(
    reporte: Reporte,
    reportViewModel: ReportViewModel,
    modifier: Modifier = Modifier,
    onEdit: (Reporte) -> Unit,
    onDelete: () -> Unit,
    onItemClick: (Reporte) -> Unit // Agregado: Función para mostrar detalles del reporte
) {
    Row(modifier = modifier.clickable { onItemClick(reporte) }) { // Modificado: Agregado el listener de clic
        Text(
            text = reporte.reportTitulo,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = reporte.reportFecha,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
        )
        IconButton(onClick = { onEdit(reporte) }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { onDelete() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}