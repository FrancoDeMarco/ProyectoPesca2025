package unpsjb.tnt.appdepesca.listado

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.database.ReporteDAO
import unpsjb.tnt.appdepesca.reporte.ReporteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    ///////////////CREAR REPORTE/////////////////////////
    fun createReport() {
        val newReportId = getNextId()
        val updatedReport = Reporte(
            reportId = newReportId,
            reportTitulo = state.reportTitle,
            reportDescripcion = state.reportDescription,
            reportFecha = state.reportDate
        )
        println("Fecha guardada: ${updatedReport.reportFecha}")///para ver en que formato se guarda la fecha cuando creo el reporte
        viewModelScope.launch {
            dao.insertReporte(updatedReport)
        }
        clearForm()
    }
    fun getNextId(): Int {
        val maxId = state.report.maxOfOrNull { it.reportId } ?: 0
        return maxId + 1
    }
    fun clearForm() {
        _state.value = _state.value.copy(
            reportId = 0,
            reportTitle = "",
            reportDescription = "",
            reportDate = ""
        )
    }
    //para agregar la imagen
    fun changeImage(uri: Uri) {
        _state.value = state.copy(reportImagenUri = uri.toString())
    }
    /////////////////////ELIMINAR REPORTE///////////////////
    fun deleteReporte(reporte: Reporte) {
        viewModelScope.launch {
            dao.deleteReporte(reporte)
        }
    }
    ////////////EDITAR REPORTE//////
    fun loadReport(reporte: Reporte) {
        _state.value = ReportState(
            reportId = reporte.reportId,
            reportTitle = reporte.reportTitulo,
            reportDescription = reporte.reportDescripcion,
            reportDate = reporte.reportFecha
        )
    }
    fun updateReport() {
        val updatedReport = Reporte(
            reportId = state.reportId,
            reportTitulo = state.reportTitle,
            reportDescripcion = state.reportDescription,
            reportFecha = state.reportDate
        )
        viewModelScope.launch {
            dao.updateReporte(updatedReport)
        }
        clearForm()
    }
    /////////////////FILTRADO DE FECHAS///////////////////////////////////
    private fun filterReportesByDates(
        reportes: List<Reporte>,
        fromDate: Date?,
        toDate: Date?
    ): List<Reporte> {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val from = fromDate?.onlyDate()
        val to = toDate?.onlyDate()

        return reportes.filter { reporte ->
            val fechaReporte: Date? = try {
                formatter.parse(reporte.reportFecha)?.onlyDate()
            } catch (e: Exception) {
                null
            }

            fechaReporte != null &&
                    (from == null || !fechaReporte.before(from)) &&
                    (to == null || !fechaReporte.after(to))
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
    fun Date.onlyDate(): Date {
        val calendar = Calendar.getInstance().apply {
            time = this@onlyDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }
}
