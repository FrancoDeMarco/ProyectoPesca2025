package unpsjb.tnt.appdepesca.reportes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.login.HeaderImage
import com.google.android.gms.maps.model.LatLng
import coil.compose.rememberAsyncImagePainter

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
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF1B2B24))
            .navigationBarsPadding() // evita conflicto con la parte inferior de la pantalla
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NombreReporte(reporte)
        Spacer(modifier = Modifier.height(8.dp))
        /******************Imagen Reporte*******/
        val imagenUrl = reporte.reportImagenUri
        if (!imagenUrl.isNullOrBlank()){
            Image(
                painter = rememberAsyncImagePainter(model = imagenUrl),
                contentDescription = "Imagen del reporte",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Fit
            )
        }
        /*********************************************/
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            FechaReporte(reporte)
            Spacer(modifier = Modifier.height(8.dp))
            DescripcionReporte(reporte)
            Spacer(modifier = Modifier.height(8.dp))
        }
        UbicacionReporte(reporte, navController) //Mapa con ubicación
    }
}

//////////////TITULO/////////////////////
@Composable
fun NombreReporte(reporte: Reporte) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HeaderImage(size = 200.dp)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = reporte.reportTitulo,
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DescripcionReporte(reporte: Reporte){
    Text(
        text = "Descripción:",
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF3E8B75),
        fontSize = 23.sp,
    )
    Text(
        text = reporte.reportDescripcion ?: " ",
        color = Color.White,
        fontSize = 23.sp,
    )
}

@Composable
fun FechaReporte(reporte: Reporte){
    Text(
        text = "Fecha: ",
        color = Color(0xFF3E8B75),
        fontSize = 23.sp // <- tamaño
    )
    Text(
        text = reporte.reportFecha,
        color = Color.White,
        fontSize = 23.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun BotonVolver(navController: NavController){
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

@Composable
fun UbicacionReporte(reporte: Reporte, navController: NavController){
    reporte.latitud?.let { lat ->
        reporte.longitud?.let { lng ->
            Text(
                text = "Ubicación:",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF3E8b75),
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            val ubicacion = LatLng(lat, lng)

            val cameraPositionState = rememberCameraPositionState{
                position = CameraPosition.fromLatLngZoom(ubicacion, 14f)
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(12.dp)),
                cameraPositionState = cameraPositionState
            ){
                Marker(
                    state = MarkerState(position = ubicacion),
                    title = "Ubicación del reporte"
                )
            }
            BotonVolver(navController)
        }
    }
}
