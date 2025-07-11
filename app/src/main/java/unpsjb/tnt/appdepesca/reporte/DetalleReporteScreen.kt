package unpsjb.tnt.appdepesca.reporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.listado.ListadoReportesViewModel


@Composable
fun DetalleReporteScreen(
    reporteId: Int,
    navController: NavController,
    listadoReportesViewModel: ListadoReportesViewModel
) {
    val reporte = listadoReportesViewModel.state.report.find { it.reportId == reporteId }

    if (reporte == null) {
        Text("Reporte no encontrado", color = Color.White)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
            .padding(16.dp)
    ) {
        Text(
            text = reporte.reportTitulo,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF3E8B75)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fecha: ${reporte.reportFecha}",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descripción:",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF3E8B75)
        )

        Text(
            text = reporte.reportDescripcion,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
