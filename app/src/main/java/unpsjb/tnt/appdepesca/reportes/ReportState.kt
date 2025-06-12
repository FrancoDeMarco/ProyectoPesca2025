package unpsjb.tnt.appdepesca.reportes

import unpsjb.tnt.appdepesca.database.Reporte

data class ReportState(
    val reportTitle: String = "",
    val reportDescription: String = "",
    val reportDate: String = "",
    val report: List<Reporte> = emptyList()
)