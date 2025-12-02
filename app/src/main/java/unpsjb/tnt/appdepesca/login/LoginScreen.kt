package unpsjb.tnt.appdepesca.login

// ======== IMPORTS ========

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.R
import kotlinx.coroutines.launch


// ======== PANTALLA PRINCIPAL ========
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1B2B24))
            .padding(16.dp),
    ) {
        Login(viewModel, navController) {
            navController.navigate("home")
        }
    }
}

// ======== LoginScreen ========
@Composable
fun Login(viewModel: LoginViewModel, navController: NavController, onLoginSuccesfull: () -> Unit) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isInvalid: Boolean by viewModel.isInvalid.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    if (isInvalid) {
            ShowAlertDialog {
                viewModel.resetInvalid()
            }

    }

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage()
            Titulo()
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
            PasswordField(password) {
                viewModel.onLoginChanged(email, it)
            }
            Spacer(modifier = Modifier.height(8.dp))  // Espacio de 8dp entre los campos
            LoginButton(loginEnable) {
                coroutineScope.launch {
                    val success = viewModel.onLoginSelected()
                    if (success) {
                        onLoginSuccesfull()
                    }
                }
            }
            RegisterButton(
                onClick = { navController.navigate("registro")}
            )
        }
    }
}

// ======== ALERT DIALOG ========
@Composable
fun ShowAlertDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Error") },
        text = { Text(text = "Usuario o contraseña incorrecta.") },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}


// ======== BOTÓN DE LOGIN ========
@Composable
fun LoginButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
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
            text = "Iniciar sesión",
            fontSize = 20.sp,
        )
    }
}

// ======== BOTÓN DE REGISTRO ========
@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3E8B75),             // activo (verde)
            //disabledContainerColor = Color(0xFF5D776C),     // grisáceo oscuro cuando está deshabilitado
            contentColor = Color.White,                     // texto en blanco
            //disabledContentColor = Color(0xFFAAAAAA)        // texto gris claro cuando está deshabilitado
        ),
    ) {
        Text(
            text = "Crear cuenta",
            fontSize = 20.sp,
        )
    }
}

// ======== CAMPO EMAIL ========
@Composable
fun EmailField(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextChanged,
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


// ======== CAMPO CONTRASEÑA ========
@Composable
fun PasswordField(password: String, onTextChanged: (String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var focusState by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onTextChanged,
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




// ======== TÍTULO ========
@Composable
fun Titulo() {
    Text(
        text = "PescApp",
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E8B75)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

// ======== IMAGEN DE CABECERA ========
@Composable
fun HeaderImage(size: Dp = 300.dp) {
    Image(
        painter = painterResource(R.drawable.fish),
        contentDescription = "Logo",
        modifier = Modifier
            .size(size)
            .padding(bottom = 16.dp)
    )
}
