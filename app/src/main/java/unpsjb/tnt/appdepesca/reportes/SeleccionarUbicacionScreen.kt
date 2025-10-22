package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun SeleccionarUbicacionScreen(
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
){
    var isMapValido = remember { mutableStateOf(false) }
    var markerPosition by remember {mutableStateOf<LatLng?>(null)}
    Mapa(isMapValido, listadoReportesViewModel, markerPosition = markerPosition, onMarkerChange = { newPosition -> markerPosition = newPosition})
}

@Composable
fun Mapa(
    isMapValid: MutableState<Boolean>,
    listadoReportesViewModel: ListadoReportesViewModel,
    markerPosition: LatLng?,
    onMarkerChange: (LatLng) -> Unit
){
    Text(
        text = "Selecciona la ubicación en el mapa",
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-34.6037, -58.3816),
            10f
        )
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp)),
        cameraPositionState = cameraPositionState,
        onMapClick = {latLng ->
            onMarkerChange(latLng)
            listadoReportesViewModel.setUbicacion(latLng.latitude, latLng.longitude)
            isMapValid.value = true
        }
    ){
        markerPosition?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Ubicación seleccionada"
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}