package unpsjb.tnt.appdepesca.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import unpsjb.tnt.appdepesca.login.HeaderImage

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: RegistroViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
            .padding(16.dp),
    ) {
        Registro(viewModel, navController)
    }
}

@Composable
fun Registro(viewModel: RegistroViewModel, navController: NavController){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.registered) {
        if (state.registered){
            navController.popBackStack() //  volver al login
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HeaderImage()
        TituloCrearCuenta()
        NombreField(username = state.username, onUsernameChange = { viewModel.onUsernameChange(it) })
        CampoEmail(email = state.email, onEmailChange = { viewModel.onEmailChange(it)})
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        CampoPassword(password = state.password, onPasswordChange = { viewModel.onPasswordChange(it)})
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        RepetirPasswordField(repeatPassword = state.repeatPassword, onRepeatPasswordChange = { viewModel.onRepeatPasswordChange(it)})
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        RegistrarseButton(loading = state.loading, onClick = { viewModel.register()})
        MensajeDeError(error = state.error)
        Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
        TengoCuentaButton(navController)
    }
}

// ======== TÍTULO ========
@Composable
fun TituloCrearCuenta() {
    Text(
        text = "Crear Cuenta",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

// ======== NOMBRE DE USUARIO ========
@Composable
fun NombreField(
    username: String,
    onUsernameChange: (String) -> Unit
){
    OutlinedTextField(
        value = username,
        onValueChange = { onUsernameChange (it) },
        label = { Text("Nombre de Usuario")},
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

// ======== EMAIL ========
@Composable
fun CampoEmail(
    email: String,
    onEmailChange: (String) -> Unit
){
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = {Text("Email")},
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

// ======== CONTRASEÑA ========
@Composable
fun CampoPassword(
    password: String,
    onPasswordChange: (String) -> Unit
){
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {Text("Contraseña")},
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

// ======== REPETIR CONTRASEÑA ========
@Composable
fun RepetirPasswordField(
    repeatPassword: String,
    onRepeatPasswordChange: (String) -> Unit
){
    OutlinedTextField(
        value = repeatPassword,
        onValueChange = onRepeatPasswordChange,
        label = {Text("Repetir Contraseña")},
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

// ======== BOTÓN PARA REGISTRARSE ========
@Composable
fun RegistrarseButton(
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = modifier.fillMaxWidth()
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text("Registrarse")
        }
    }
}

// ======== MENSAJES DE ERROR ========
@Composable
fun MensajeDeError(error: String?){
    if (error != null) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error
        )
    }
}

// ======== VOLVER ========
@Composable
fun TengoCuentaButton(navController: NavController){
    TextButton(onClick = {
        navController.popBackStack()
    }){
        Text("Ya tengo cuenta")
    }
}
