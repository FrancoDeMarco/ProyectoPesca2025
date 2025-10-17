package unpsjb.tnt.appdepesca.reportes

import unpsjb.tnt.appdepesca.database.Reporte

data class ReportState(
    val reportId: Int = 0, // Empieza en 0, se asigna con getNextId() cuando se crea
    val reportTitle: String = "",
    val reportDescription: String = "",
    val reportDate: String = "",
    val report: List<Reporte> = emptyList(),
    val reportImagenUri: String? = null,
    val reportLat: Double? = null,
    val reportLng: Double? = null
)