//package com.example.basicscodelab.ui.theme.database
package unpsjb.tnt.appdepesca.database


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Reporte::class], version = 5, exportSchema = false)
abstract class PescaRoomDatabase : RoomDatabase() {
    abstract val pescaDAO: ReporteDAO
}
