package com.example.proyectopesca2025.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConcursoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConcurso(concurso: Concurso)

    @Update
    suspend fun updateConcurso(concurso: Concurso)

    @Delete
    suspend fun deleteConcurso(concurso: Concurso)

    @Query("SELECT * FROM concurso_table")
    fun getAllConncurso(): Flow<List<Concurso>>

    @Query("SELECT * FROM concurso_table WHERE concursoId = :id")
    suspend fun getconcursoById(id: Int): Concurso?
}
