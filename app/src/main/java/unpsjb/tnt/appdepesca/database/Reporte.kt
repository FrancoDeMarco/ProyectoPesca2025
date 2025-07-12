package unpsjb.tnt.appdepesca.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reporte_table")
data class Reporte(
    @PrimaryKey(autoGenerate = true) var reportId: Int,
    @ColumnInfo val reportTitulo: String,
    @ColumnInfo val reportDescripcion: String,
    @ColumnInfo val reportFecha: String,
    @ColumnInfo val reportImagenUri: String? = null,
    val latitud: Double?,
    val longitud: Double?
)
