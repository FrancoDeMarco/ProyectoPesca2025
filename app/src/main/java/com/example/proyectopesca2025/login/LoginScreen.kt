package com.example.proyectopesca2025.login


// ======== IMPORTS ========
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import unpsjb.tnt.appdepesca.R
import com.example.proyectopesca2025.theme.ProyectoPesca2025Theme
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview


// ======== PANTALLA PRINCIPAL ========
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0xFF9EEE9E))
            .padding(16.dp),
    ) {
        Login(Modifier.align(Alignment.Center), viewModel) {
            navController.navigate("home")
        }
    }
}

// ======== LOGIN ========
@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, onLoginSuccesfull: () -> Unit) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordVisible = remember { mutableStateOf(false) }
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isInvalid: Boolean by viewModel.isInvalid.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    if (isInvalid) {
        Box(Modifier.fillMaxSize()) {
            showAlertDialog()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderImage()
            Titulo()
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(password, passwordVisible.value) {
                viewModel.onLoginChanged(email, it)
            }
            Spacer(modifier = Modifier.padding(8.dp))
            ForgotPassword(Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(loginEnable) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                    if (validarCredenciales(email, password)) {
                        onLoginSuccesfull()
                    } else {
                        viewModel.credencialesNoValidas()
                    }
                }
            }
        }
    }
}

// ======== ALERT DIALOG ========
@Composable
fun showAlertDialog() {
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Error") },
            text = { Text(text = "Usuario o contraseña incorrecta.") },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

// ======== VALIDACIÓN ========
fun validarCredenciales(usuario: String, contraseña: String): Boolean {
    return usuario == "admin@gmail.com" && contraseña == "administrador"
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
            containerColor = Color(0xFFFF5B03),
            disabledContainerColor = Color(0xFFF78058),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text("Iniciar sesión")
    }
}

// ======== "¿OLVIDASTE TU CONTRASEÑA?" ========
@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFB9600)
    )
}

// ======== CAMPO CONTRASEÑA ========
@Composable
fun PasswordField(password: String, passwordVisible: Boolean, onTextChanged: (String) -> Unit) {
    val visible = remember { mutableStateOf(passwordVisible) }

    OutlinedTextField(
        value = password,
        onValueChange = onTextChanged,
        placeholder = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        visualTransformation = if (visible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible.value = !visible.value }) {
                Icon(
                    imageVector = if (visible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (visible.value) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

// ======== CAMPO EMAIL ========
@Composable
fun EmailField(email: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onTextChanged,
        placeholder = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

// ======== TÍTULO ========
@Composable
fun Titulo() {
    Text(
        text = "Bienvenido a la App de Pesca",
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

// ======== IMAGEN DE CABECERA ========
@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(R.drawable.pescado),
        contentDescription = "Logo",
        modifier = Modifier
            .size(100.dp)
            .padding(bottom = 16.dp)
    )
}


@Composable
fun LoginScreenPreviewUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF9EEE9E))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderImage()
        Titulo()
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email = "", onTextChanged = {})
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password = "", passwordVisible = false, onTextChanged = {})
        Spacer(modifier = Modifier.padding(8.dp))
        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton(enabled = true, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenVisualPreview() {
    ProyectoPesca2025Theme {
        LoginScreenPreviewUI()
    }
}