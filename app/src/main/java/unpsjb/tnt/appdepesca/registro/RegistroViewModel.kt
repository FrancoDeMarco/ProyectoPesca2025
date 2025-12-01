package unpsjb.tnt.appdepesca.registro

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val registered: Boolean = false
)


class RegistroViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state

    fun onUsernameChange(newValue: String){
        _state.value = _state.value.copy(
            username = newValue,
            error = null
        )
    }
    fun onEmailChange(newValue: String){
        _state.value = _state.value.copy(
            email = newValue,
            error = null
        )
    }
    fun onPasswordChange(newValue: String){
        _state.value = _state.value.copy(
            password = newValue,
            error = null
        )
    }
    fun onRepeatPasswordChange(newValue: String){
        _state.value = _state.value.copy(
            repeatPassword = newValue,
            error = null,
        )
    }

    fun register() {
        val s = _state.value
        //1) Validar campos vacíos
        if (s.username.isBlank() || s.email.isBlank() || s.password.isBlank() || s.repeatPassword.isBlank()) {
            _state.value = s.copy(error = "Todos los campos son obligatorios")
            return
        }
        //2) Validar contraseñas iguales
        if (s.password != s.repeatPassword){
            _state.value = s.copy(error = "Las contraseñas no coinciden")
            return
        }
        //3) Validar Email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()){
            _state.value = s.copy(error = "Email inválido")
            return
        }
        _state.value = s.copy(loading = true, error = null)
        auth.createUserWithEmailAndPassword(s.email, s.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser!!.uid
                    //Datos a guardar en Firestore
                    val data = mapOf(
                        "uid" to uid,
                        "username" to s.username,
                        "email" to s.email
                    )
                    firestore.collection("usuarios").document(uid)
                        .set(data)
                        .addOnSuccessListener {
                            _state.value = _state.value.copy(
                                loading = false,
                                registered = true
                            )
                        }
                        .addOnFailureListener { e ->
                            _state.value = _state.value.copy(
                                loading = false,
                                error = e.message
                            )
                        }
                } else {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = task.exception?.message ?: "Error desconocido"
                    )
                }
            }
    }
}
