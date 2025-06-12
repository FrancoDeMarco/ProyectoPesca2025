package unpsjb.tnt.appdepesca.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.R

/****************Código de la pantalla*************/
@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0xFF9EEE9E))
            .padding(16.dp),
    ) {
        Login(Modifier.align(Alignment.Center), viewModel)
        { navController.navigate("home") }
    }
}
/*************************************************************/

/****************Código del Login****************/
@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, onLoginSuccesfull: () -> Unit) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val passwordVisible: MutableState<Boolean> = remember { mutableStateOf(false) }
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
            HeaderImage()                   /***Logo de Cabecera***/
            Titulo()                        /***Titulo de Cabecera***/
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChanged(it, password) }
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(password, passwordVisible.value) { newValue -> viewModel.onLoginChanged(email, newValue) }
            Spacer(modifier = Modifier.padding(8.dp))
            ForgotPassword(Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(viewModel, loginEnable) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                    if (validarCredenciales(email, password)) {
                        onLoginSuccesfull()
                        /*********La contraseña o el usuario son correctos.********/
                    } else {
                        viewModel.credencialesNoValidas()
                       /*********La contraseña o el usuario son incorrectos.******/
                    }
                }
            }
        }
    }
}

/*************************************************************/


@Composable
fun showAlertDialog() {
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Error") },
                text = { Text(text = "Usuario o contraseña incorrecta.") },
                buttons = {
                    Button(
                        onClick = { showDialog.value = false }
                    ) {
                        Text(text = "Cerrar")
                    }
                }
            )
        }
    }
}

fun validarCredenciales(usuario: String, contraseña: String): Boolean {
    val usuarioCorrecto = "admin@gmail.com"  //usuario y contraseña harcodeados
    val contraseñaCorrecta = "administrador"

    return usuario == usuarioCorrecto && contraseña == contraseñaCorrecta
}


@Composable
fun LoginButton(viewModel: LoginViewModel, loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = {
            onLoginSelected()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFF5B03),
            disabledBackgroundColor = Color(0xFFF78058),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable

    ) {
        Text(text = "Iniciar sesión")
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFB9600)
    )
}

@Composable
fun PasswordField(password: String, passwordVisible: Boolean, onTextFieldChanged: (String) -> Unit) {
    val localPasswordVisible = remember { mutableStateOf(passwordVisible) }

    OutlinedTextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        visualTransformation = if (localPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { localPasswordVisible.value = !localPasswordVisible.value }) {
                Icon(
                    imageVector = if (localPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (localPasswordVisible.value) "Hide password" else "Show password"
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            backgroundColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}



@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email, onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            backgroundColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

/****************Código del título de Cabecera*************/
@Composable
fun Titulo() {
    Text(
        text = "Bienvenido a la App de Pesca",
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
/*************************************************************/


/****************Código de la imagen de Cabecera*************/
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
/*************************************************************/