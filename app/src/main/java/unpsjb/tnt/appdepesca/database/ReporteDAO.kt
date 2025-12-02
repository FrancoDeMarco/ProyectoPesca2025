package unpsjb.tnt.appdepesca.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReporte(reporte: Reporte)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportes(reportes: List<Reporte>)

    @Update
    suspend fun updateReporte(reporte: Reporte)

    @Delete
    suspend fun deleteReporte(reporte: Reporte)

    @Query("DELETE FROM reporte_table")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(reportes: List<Reporte>){
        clearAll()
        insertReportes(reportes)
    }

    @Query("SELECT * FROM reporte_table")
    fun getAllReportes(): Flow<List<Reporte>>

    @Query("SELECT * FROM reporte_table WHERE reportId = :id")
    suspend fun getReporteById(id: Int): Reporte?
}
