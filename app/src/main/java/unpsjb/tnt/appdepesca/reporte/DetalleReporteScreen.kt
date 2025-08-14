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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.R
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.login.HeaderImage


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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NombreReporte(reporte)
        Spacer(modifier = Modifier.height(8.dp))
        ImagenReporte(reporte)
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
        BotonVolver(navController)
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
fun ImagenReporte(reporte: Reporte) {
    reporte.reportImagenUri?.let {
        ImagenDesdeUri(it)
    }
}

@Composable
fun ImagenDesdeUri(uriString: String?) {
    val context = LocalContext.current
    if (uriString != null) {
        val uri = Uri.parse(uriString)
        val inputStream = try {
            context.contentResolver.openInputStream(uri)
        } catch (e: Exception) {
            null
        }
        inputStream?.use {
            val bitmap = BitmapFactory.decodeStream(it)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen del reporte",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)    // Cambia el tamano de la imagen pero sin que esta se vea cortada
                        .aspectRatio(it.width.toFloat() / it.height.toFloat())  // Mantiene la proporción original de la imagen
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Fit //Muestra la imagen entera, sin recortar ni deformar
                )
            }
        }
    }
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
        text = reporte.reportDescripcion,
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
        text = "${reporte.reportFecha}",
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
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonModifier = Modifier
            Button(
                onClick = { navController.popBackStack() },
                modifier = buttonModifier.border(
                    width = 2.dp,               // grosor del borde
                    color = Color(0xFF3E8B75),  // color del borde
                    shape = RectangleShape      // importante: que coincida con el shape del botón
                ),

                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
                shape = RectangleShape // <- esto lo hace cuadrado
            ) {
                Image(
                    painter = painterResource(R.drawable.retroceso), //nombre de la imagen
                    contentDescription = "Retroceso",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}
