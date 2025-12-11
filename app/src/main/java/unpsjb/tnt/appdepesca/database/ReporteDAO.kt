package unpsjb.tnt.appdepesca.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReporte(reporte: Reporte): Long

    @Update
    suspend fun updateReporte(reporte: Reporte)

    @Delete
    suspend fun deleteReporte(reporte: Reporte)

    @Query("SELECT * FROM reporte_table")
    fun getAllReportes(): Flow<List<Reporte>>

    @Query("SELECT * FROM reporte_table WHERE usuarioId = :uid")
    fun getReportesByUsuario(uid: String): Flow<List<Reporte>>


    //Permite borrar todos los reportes locales y cargarlos desde Firestore
    @Query("DELETE FROM reporte_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reportes: List<Reporte>)

    @Transaction
    suspend fun replaceAll(reportes: List<Reporte>) {
        deleteAll()
        insertAll(reportes)
    }

    @Query("SELECT * FROM reporte_table WHERE usuarioId = :uid ORDER BY reportFecha DESC")
    fun getReportesByUsuarioOrdenadosPorFechaDesc(uid: String): Flow<List<Reporte>>

    @Query("SELECT * FROM reporte_table WHERE usuarioId = :uid ORDER BY reportFecha ASC")
    fun getReportesByUsuarioOrdenadosPorFechaAsc(uid: String): Flow<List<Reporte>>

}