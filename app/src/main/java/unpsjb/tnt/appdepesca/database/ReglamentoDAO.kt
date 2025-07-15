package unpsjb.tnt.appdepesca.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReglamentoDAO {

    @Query("SELECT * FROM reglamento_table")
    fun getAllReglamento(): Flow<List<Reglamento>>

    @Query("SELECT * FROM reglamento_table WHERE reglamentoId = :id")
    suspend fun getReglamentoById(id: Int): Reglamento?
}
