package unpsjb.tnt.appdepesca.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReporte(reporte: Reporte)

    @Update
    suspend fun updateReporte(reporte: Reporte)

    @Delete
    suspend fun deleteReporte(reporte: Reporte)

    @Query("SELECT * FROM reporte_table")
    fun getAllReportes(): Flow<List<Reporte>>

    @Query("SELECT * FROM reporte_table WHERE usuarioId = :uid")
    fun getReportesByUsuario(uid: String): Flow<List<Reporte>>
}