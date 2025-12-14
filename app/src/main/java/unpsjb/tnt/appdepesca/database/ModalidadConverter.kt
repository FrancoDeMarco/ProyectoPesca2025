package unpsjb.tnt.appdepesca.database

import androidx.room.TypeConverter

class ModalidadConverter {
    @TypeConverter
    fun fromModalidad(modalidad: ModalidadPesca): String {
        return modalidad.name
    }

    @TypeConverter
    fun toModalidad(value: String): ModalidadPesca{
        return ModalidadPesca.valueOf(value)
    }
}