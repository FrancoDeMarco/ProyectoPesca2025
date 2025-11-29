package unpsjb.tnt.appdepesca.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val registered: Boolean = false
)

class RegistroViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state

    fun onEmailChange(newValue: String){
        _state.value = _state.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String){
        _state.value = _state.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String){
        _state.value = _state.value.copy(repeatPassword = newValue)
    }

    fun clearError(){
        _state.value = _state.value.copy(error = null)
    }

    fun register() {
        val s = _state.value

        if (s.email.isBlank() || s.password.isBlank() || s.repeatPassword.isBlank()) {
            _state.value = s.copy(error = "Todos los campos son obligatorios")
            return
        }

        if (s.password != s.repeatPassword){
            _state.value = s.copy(error = "Las contraseñas no coinciden")
            return
        }

        _state.value = s.copy(loading = true, error = null)

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(s.email, s.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _state.value = _state.value.copy(
                            loading = false,
                            registered = true
                        )
                    } else {
                        _state.value = _state.value.copy(
                            loading = false,
                            error = task.exception?.message ?: "Error desconocido"
                        )
                    }
                }
        }
    }
}
