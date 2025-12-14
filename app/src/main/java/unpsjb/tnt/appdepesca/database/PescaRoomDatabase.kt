//package com.example.basicscodelab.ui.theme.database
package unpsjb.tnt.appdepesca.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Reporte::class], version = 6, exportSchema = false)
@TypeConverters(ModalidadConverter::class)
abstract class PescaRoomDatabase : RoomDatabase() {
    abstract val pescaDAO: ReporteDAO
}
