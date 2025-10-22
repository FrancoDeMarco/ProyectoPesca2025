package unpsjb.tnt.appdepesca.reportes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun SeleccionarFotosScreen(
    navController: NavController,
    viewModel: ListadoReportesViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1B2B24))
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AgregarFotoButton(viewModel = viewModel)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 24.dp)
            ) {
                VolverButton(
                    navController,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-12).dp, y = (-32).dp)
                )
            }
            SiguienteMapa(navController, modifier = Modifier)
        }
    }
}

@Composable
fun AgregarFotoButton(
    viewModel: ListadoReportesViewModel
){
    val uri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { selectedUri ->
        selectedUri?.let {
            uri.value = it
            viewModel.changeImage(it)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { launcher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E8B75))
        ) {
            Icon(Icons.Default.Photo, contentDescription = "Seleccionar imagen")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Seleccionar Imagen")
        }
        uri.value?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Imagen del reporte",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            //isImagenValid.value = viewModel.state.reportImagenUri != null//Con esto supuestamente ya no se pierde la imagen al volver a atrás
        }
    }
}

@Composable
fun SiguienteMapa(
    navController: NavController,
    modifier: Modifier
){
    Button(
        onClick = { navController.navigate("seleccionar_fotos") },
        modifier = modifier.border(
            width = 2.dp,               // grosor del borde
            color = Color(0xFF3E8B75),  // color del borde
            shape = RectangleShape      // importante: que coincida con el shape del botón
        ),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B24)),
        shape = RectangleShape  //esto lo hace cuadrado
    ) {
        Text(
            text = "Siguiente",
            fontSize = 30.sp // tamaño
        )
    }
}