package unpsjb.tnt.appdepesca.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ResetPasswordScreen(
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("")}
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false)}
    var errorMessage by remember {mutableStateOf<String?>(null)}

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2B24))
            .padding(16.dp)
    ) {
        if (isLoading){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Recuperar contraseña",
                color = Color(0xFF3E8B75),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height((24.dp)))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color(0XFF3E8B75)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1B2B24),
                    unfocusedContainerColor = Color(0xFF1B2B24),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color(0XFF3E8B75),
                )
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true
                        auth
                            .sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    showDialog = true
                                    errorMessage = null
                                } else {
                                    errorMessage = "No se encontró una cuenta con este correo."
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3E8B75)
                )
            ) {
                Text("Enviar mail de recuperación")
            }
            errorMessage?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Color.Red)
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Correo enviado") },
                text = {
                    Text("Revisá tu casilla y seguí el enlace para restablecer la contraseña.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("Volver", color = Color(0XFF3E8B75))
                    }
                }
            )
        }
    }
}
