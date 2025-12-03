package unpsjb.tnt.appdepesca.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
        Spacer(modifier = Modifier.height(16.dp))  // Espacio de 16dp entre los campos
        RegistrarseButton(botonHabilitado = state.formValid, onClick = { viewModel.register()})
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = TextStyle(fontSize = 20.sp), // Cambia el tamaño del texto ingresado
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = "Nombre de Usuario",
                color = Color(0xFF3E8B75),
                fontSize = 20.sp
            )
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )
    )
}

// ======== EMAIL ========
@Composable
fun CampoEmail(email: String, onEmailChange: (String) -> Unit){
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = TextStyle(fontSize = 20.sp), // Cambia el tamaño del texto ingresado
        label = {
            Text(
                text = "Email",
                color = Color(0xFF3E8B75),
                fontSize = 20.sp
            )
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )
    )
}

// ======== CONTRASEÑA ========
@Composable
fun CampoPassword(
    password: String,
    onPasswordChange: (String) -> Unit
){
    var visible by remember { mutableStateOf(false) }
    var focusState by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier =  Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState = it.isFocused }, // <- Detecta foco,
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        textStyle = TextStyle(fontSize = 20.sp), // Cambia el tamaño del texto ingresado
        label = {
            Text(
                text = "Contraseña",
                color = Color(0xFF3E8B75),
                fontSize = 20.sp
            )
        },
        trailingIcon = { // ícono del ojo
            if (focusState) { // <- Solo muestra el ícono si hay foco
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.White // Esto lo hace blanco
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )

    )
}

// ======== REPETIR CONTRASEÑA ========
@Composable
fun RepetirPasswordField(
    repeatPassword: String,
    onRepeatPasswordChange: (String) -> Unit
){
    var visible by remember { mutableStateOf(false) }
    var focusState by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = repeatPassword,
        onValueChange = onRepeatPasswordChange,
        modifier =  Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState = it.isFocused }, // <- Detecta foco,
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        textStyle = TextStyle(fontSize = 20.sp), // Cambia el tamaño del texto ingresado
        label = {Text(
            text = "Repetir Contraseña",
            color = Color(0xFF3E8B75),
            fontSize = 20.sp
        )
        },
        trailingIcon = { // ícono del ojo
            if (focusState) { // <- Solo muestra el ícono si hay foco
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.White // Esto lo hace blanco
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1B2B24),
            unfocusedContainerColor = Color(0xFF1B2B24),
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            focusedIndicatorColor = Color(0xFFFFFFFF),
            unfocusedIndicatorColor = Color(0xFF3E8B75),
        )

    )
}

// ======== BOTÓN PARA REGISTRARSE ========
@Composable
fun RegistrarseButton(botonHabilitado: Boolean, onClick: () -> Unit,
){
    Button(
        onClick = onClick,
        enabled = botonHabilitado,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3E8B75),             // activo (verde)
            disabledContainerColor = Color(0xFF5D776C),     // grisáceo oscuro cuando está deshabilitado
            contentColor = Color.White,                     // texto en blanco
            disabledContentColor = Color(0xFFAAAAAA)        // texto gris claro cuando está deshabilitado
        ),
    ) {
        Text(
            text = "Registrarse",
            fontSize = 20.sp,
        )
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
