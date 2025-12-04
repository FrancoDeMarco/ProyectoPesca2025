package unpsjb.tnt.appdepesca.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import unpsjb.tnt.appdepesca.usuario.UsuarioViewModel

@Composable
fun LayoutBase(
    usuarioVM: UsuarioViewModel,
    content: @Composable () -> Unit
){
    val username = usuarioVM.username.collectAsState().value
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        UserCorner(username)
    }
}//
