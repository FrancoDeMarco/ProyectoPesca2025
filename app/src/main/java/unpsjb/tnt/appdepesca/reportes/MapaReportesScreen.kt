package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import unpsjb.tnt.appdepesca.database.Reporte
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun MapaReportesScreen(
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
){
    val reportes: List<Reporte> = listadoReportesViewModel.state.report
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-38.4161, -63.6167), 4f)
    }
    Box(modifier = Modifier.fillMaxSize()){
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ){
            reportes.forEach { reporte ->
                val lat = reporte.latitud
                val lng = reporte.longitud
                if (lat != null && lng != null){
                    Marker(
                        state = MarkerState(position = LatLng(lat, lng)),
                        title = reporte.reportTitulo,
                        snippet = reporte.reportDescripcion,
                        onClick = {
                            navController.navigate("detalle_reporte/${reporte.reportId}")
                            true
                        }
                    )
                }
            }
        }
        BotonVolverMapa(navController)
    }
}

@Composable
fun BotonVolverMapa(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(64.dp)
                .background(
                    color = Color.White.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retroceso",
                tint = Color(0xFF3E8B75) // color del ícono
            )
        }
    }
}
