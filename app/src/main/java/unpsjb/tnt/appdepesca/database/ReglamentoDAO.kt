package unpsjb.tnt.appdepesca.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RelgamentoDAO {

    @Query("SELECT * FROM concurso_table")
    fun getAllReglamento(): Flow<List<Reglamento>>

    @Query("SELECT * FROM concurso_table WHERE concursoId = :id")
    suspend fun getReglamentoById(id: Int): Reglamento?
}