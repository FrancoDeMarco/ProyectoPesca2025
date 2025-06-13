package unpsjb.tnt.appdepesca.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concurso_table")
data class Concurso(
    @PrimaryKey(autoGenerate = true) val concursoId: Int,
    @ColumnInfo val concursoNombre: String,
    @ColumnInfo val concursoLugar: String,
    @ColumnInfo val concursoFecha: String,
    @ColumnInfo val concursoDescripcion: String,
    @ColumnInfo val concursoBases: String,
    @ColumnInfo val concursoPremio: String,
)

