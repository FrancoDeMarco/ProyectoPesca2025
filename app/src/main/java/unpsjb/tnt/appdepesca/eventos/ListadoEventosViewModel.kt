package unpsjb.tnt.appdepesca.eventos

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListadoEventosViewModel: ViewModel(){
    private val firestore = FirebaseFirestore.getInstance()

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private val _categoriaSeleccionada = MutableStateFlow("todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    init {
        firestore.collection("eventos")
            .addSnapshotListener { snapshot, error ->
                if(error != null) return@addSnapshotListener

                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Evento(
                        id = doc.id,
                        titulo = data["titulo"] as? String?: "",
                        descripcion = data["descripcion"] as? String?: "",
                        categoria = data["categoria"] as? String?: "",
                        lugar = data["lugar"] as? String,
                        fecha = data["fecha"] as? String,
                        bases = data["bases"] as? String,
                        premio = data["premio"] as? String,
                        enlaces = (data["enlaces"] as? List<*>)?.filterIsInstance<String>()?:emptyList()
                        )
                } ?: emptyList()
                _eventos.value = lista
            }
    }
    fun filtrarPorCategoria(cat: String){
        _categoriaSeleccionada.value = cat

    }
}