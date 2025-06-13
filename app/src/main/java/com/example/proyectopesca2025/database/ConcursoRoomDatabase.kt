package com.example.proyectopesca2025.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Concurso::class], version = 2, exportSchema = false)
abstract class ConcursoRoomDatabase : RoomDatabase() {
    abstract val concursoDAO: ConcursoDAO

}