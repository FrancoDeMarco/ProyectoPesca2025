package unpsjb.tnt.appdepesca.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reporte_table")
data class Reporte(
    @PrimaryKey(autoGenerate = true) var reportId: Int,
    @ColumnInfo val reportTitulo: String,
    @ColumnInfo val reportDescripcion: String?,// Ahora "Descripción" es opcional
    @ColumnInfo val reportFecha: String,
    @ColumnInfo val reportImagenUri: String?,
    @ColumnInfo val latitud: Double?,
    @ColumnInfo val longitud: Double?,
    @ColumnInfo val reportModalidad: ModalidadPesca,
    @ColumnInfo val usuarioId: String // Nueva columna para filtrar reportes por usuario
)
