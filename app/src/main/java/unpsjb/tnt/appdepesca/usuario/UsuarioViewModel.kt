package unpsjb.tnt.appdepesca.usuario

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import unpsjb.tnt.appdepesca.database.Usuario

class UsuarioViewModel : ViewModel() {
    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()
    private val db = FirebaseFirestore.getInstance()
    private val usuariosCollection = db.collection("usuarios")

    suspend fun verificarYCrearUsuariosSiEsNecesario(firebaseUser: FirebaseUser): Boolean {
        return try {
            val uid = firebaseUser.uid
            val documentoUsuario = usuariosCollection.document(uid).get().await()
            //Si el documento NO existe, es un usuario nuevo
            if (!documentoUsuario.exists()) {
                val nuevoUsuario = Usuario(
                    id = uid,
                    nombre = firebaseUser.displayName ?: "Sin Nombre", // Usamos el nombre Google
                    email = firebaseUser.email ?: "",
                    username = firebaseUser.displayName ?: "Usuario"
                )
                usuariosCollection.document(uid).set(nuevoUsuario).await()
            }
            cargarUsuario(uid) //cargarUsuario ahora es suspend, así que al esperamos directamente
            true // Informamos del éxito
        } catch (e: Exception) {
            e.printStackTrace()
            false // Informamos del fallo
        }
    }

    suspend fun cargarUsuario(uid: String): Boolean {
        return try {
            val doc = usuariosCollection.document(uid).get()
                .await() //Usamos await para obtener el resultado
            if (doc != null && doc.exists()) {
                _username.value = doc.getString("nombre")
                true // La carga fue exitosa
            } else {
                _username.value = null
                false // La carga falló por una excepción
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _username.value = null
            false // La carga falló por una excepción
        }
    }
}
