//package com.example.basicscodelab.ui.theme.database
package com.example.proyectopesca2025.database


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Reporte::class], version = 2, exportSchema = false)
abstract class PescaRoomDatabase : RoomDatabase() {
    abstract val pescaDAO: ReporteDAO

}