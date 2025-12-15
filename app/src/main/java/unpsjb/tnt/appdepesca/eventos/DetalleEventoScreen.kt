package unpsjb.tnt.appdepesca.eventos

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.reportes.BotonVolver
import unpsjb.tnt.appdepesca.ui.components.InfoCard

@Composable
fun DetalleEventoScreen(
    eventoId: String,
    listadoEventosViewModel: ListadoEventosViewModel,
    navController: NavController
) {
    val evento = listadoEventosViewModel.eventos
        .collectAsState()
        .value
        .find { it.id == eventoId }
    Box(
        modifier = Modifier
            .background(Color(0xFF1B2B24))
            .fillMaxSize()
    ) {
        // Si el evento no cargó, muestra un mensaje
        if (evento == null) {
            Text(
                text = "Cargando evento...",
                color = Color.White,
                fontSize = 24.sp
            )
        }
        // Esto solo cuando el evento existe
        LazyColumn(Modifier.padding(16.dp)) {
            item {
                Text(
                    text = evento?.titulo ?: "",
                    fontSize = 28.sp,
                    color = Color(0XFF3E8B75),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
            }
            evento?.lugar
                ?.takeIf { it.isNotBlank() } // Elimina el campo si está vacío
                ?.let { item { InfoCard("Lugar", it) } }
            evento?.fecha
                ?.takeIf { it.isNotBlank() }
                ?.let { item { InfoCard("Fecha", it) } }
            evento?.descripcion
                ?.takeIf { it.isNotBlank() }
                ?.let { item { InfoCard("Descripcion", it) } }
            evento?.bases
                ?.takeIf { it.isNotBlank() }
                ?.let { item { InfoCard("Bases", it) } }
            evento?.premio
                ?.takeIf { it.isNotBlank() }
                ?.let { item { InfoCard("Premio", it) } }
            evento?.enlaces
                ?.takeIf { it.isNotEmpty() }
                ?.let {
                    item {
                        InfoCardLinks("Enlaces", it)
                    }
                }
        }
        BotonVolver(navController)
    }
}

@Composable
fun InfoCardLinks(
    titulo: String,
    enlaces: List<String>
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF243830))
            .padding(12.dp)
    ) {
        Text(
            text = titulo,
            color = Color(0xFF3E8B75),
            fontSize = 18.sp
        )
        Spacer(Modifier.height(8.dp))
        enlaces.forEach { enlace ->
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "URL",
                    annotation = enlace

                )
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF3E8B75),
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(enlace)
                }
                pop()
            }
            ClickableText(
                text = annotatedString,
                style = TextStyle(fontSize = 15.sp),
                        onClick = { offset ->
                    annotatedString
                        .getStringAnnotations("URL", offset, offset)
                        .firstOrNull()
                        ?.let { annotation ->
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(annotation.item)
                            )
                            context.startActivity(intent)
                        }
                }
            )
            Spacer(Modifier.height(6.dp))
        }
    }
}
