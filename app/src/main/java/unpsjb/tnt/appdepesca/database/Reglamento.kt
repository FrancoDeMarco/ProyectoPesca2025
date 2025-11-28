package unpsjb.tnt.appdepesca.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reglamento_table")
data class Reglamento(
    @PrimaryKey(autoGenerate = true) val reglamentoId: Int,
    @ColumnInfo val reglamentoNombre: String,
    @ColumnInfo val reglamentoLugar: String,
    @ColumnInfo val reglamentoFecha: String,
    @ColumnInfo val reglamentoDescripcion: String,
    @ColumnInfo val reglamentoEnlaces: List<String>
)