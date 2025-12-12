package unpsjb.tnt.appdepesca.eventos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evento_table")
data class Evento(
    @PrimaryKey val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",// "concurso", "reglamento", "sorteo", "aviso"
    val lugar: String? = null,
    val fecha: String? = null,
    val bases: String? = null,
    val premio: String? = null,
    val enlaces: List<String>? = null
)