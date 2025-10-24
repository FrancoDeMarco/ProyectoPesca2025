package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import unpsjb.tnt.appdepesca.reglamentos.BotonVolver



@Composable
fun SeleccionarUbicacionCrearScreen(
    listadoReportesViewModel: ListadoReportesViewModel,
    navController: NavController
) {
    var isMapValido = remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1B2B24))
                .systemBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CrearUbicacion(
                isMapValido,
                listadoReportesViewModel,
                markerPosition = markerPosition,
                onMarkerChange = { newPosition -> markerPosition = newPosition })
            AgregarButton(enabled = isMapValido.value) {
                listadoReportesViewModel.createReport()
                listadoReportesViewModel.clearForm()
                navController.navigate("reportes")
            }
        }
        BotonVolver(
            navController,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 24.dp, y = (-32).dp)
        )
    }
}

@Composable
fun CrearUbicacion(
    isMapValid: MutableState<Boolean>,
    listadoReportesViewModel: ListadoReportesViewModel,
    markerPosition: LatLng?,
    onMarkerChange: (LatLng) -> Unit
){
    Text(
        text = "Selecciona la ubicación en el mapa",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
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
            .fillMaxHeight(0.9f)
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

