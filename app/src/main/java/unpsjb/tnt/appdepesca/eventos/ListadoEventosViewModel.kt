package unpsjb.tnt.appdepesca.eventos

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
                    doc.toObject(Evento::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                _eventos.value = lista
                }
            }
    fun filtrarPorCategoria(cat: String){
        _categoriaSeleccionada.value = cat

    }
}