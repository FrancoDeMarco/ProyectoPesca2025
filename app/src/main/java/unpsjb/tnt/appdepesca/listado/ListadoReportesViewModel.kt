package unpsjb.tnt.appdepesca.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.database.ReporteDAO
import unpsjb.tnt.appdepesca.reporte.ReporteViewModel
import java.util.Date

class ListadoReportesViewModel(
    private val dao: ReporteDAO
) : ViewModel() {
    private val _state = mutableStateOf(ReportState())
    val state: ReportState
        get() = _state.value

    private val _fechasFiltro = MutableStateFlow<Pair<Date?, Date?>>(null to null)
    val fechasFiltro: StateFlow<Pair<Date?, Date?>> = _fechasFiltro

    init {
        viewModelScope.launch {
            dao.getAllReportes()
                .combine(fechasFiltro) { reportes, fechas ->
                    filterReportesByDates(reportes, fechas.first, fechas.second)
                }
                .collectLatest { reportes ->
                    _state.value = state.copy(report = reportes)
                }
        }
    }

    fun changeTitle(title: String) {
        _state.value = state.copy(reportTitle = title)
    }

    fun changeDescription(description: String) {
        _state.value = state.copy(reportDescription = description)
    }

    fun changeDate(date: String) {
        _state.value = state.copy(reportDate = date)
    }

    fun createReport() {
        val newReportId = getNextId()
        val updatedReport = Reporte(
            reportId = newReportId,
            reportTitulo = state.reportTitle,
            reportDescripcion = state.reportDescription,
            reportFecha = state.reportDate
        )
        viewModelScope.launch {
            dao.insertReporte(updatedReport)
        }
        _state.value = state.copy(
            reportTitle = "",
            reportDescription = "",
            reportDate = ""
        )
    }

    fun deleteReporte(reporte: Reporte) {
        viewModelScope.launch {
            dao.deleteReporte(reporte)
        }
    }

    fun getNextId(): Int {
        val maxId = state.report.maxOfOrNull { it.reportId } ?: 0
        return maxId + 1
    }

    private fun filterReportesByDates(
        reportes: List<Reporte>,
        fromDate: Date?,
        toDate: Date?
    ): List<Reporte> {
        if (fromDate == null && toDate == null) {
            return reportes
        }
        return reportes.filter { reporte ->
            val fechaReporte = reporte.reportFecha
            (fromDate == null || fechaReporte >= fromDate.toString()) &&
                    (toDate == null || fechaReporte <= toDate.toString())
        }
    }


    //Función para obtener todos los reportes filtrados por fechas
    fun getAllReportesFlow(): Flow<List<Reporte>> {
        return dao.getAllReportes()
    }

    //Función para establecer las fechas de filtro
    fun setFechasFiltro(fromDate: Date?, toDate: Date?) {
        _fechasFiltro.value = fromDate to toDate
    }
}
