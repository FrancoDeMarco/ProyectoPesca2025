package unpsjb.tnt.appdepesca.usuario

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsuarioViewModel : ViewModel() {
    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    fun cargarUsuario(uid: String){
        Firebase.firestore.collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _username.value = doc.getString("username")
            }
    }

    fun limpiar() {
        _username.value = null
    }
}