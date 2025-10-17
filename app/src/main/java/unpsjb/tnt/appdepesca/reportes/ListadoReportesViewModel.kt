package unpsjb.tnt.appdepesca.reportes

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.database.ReporteDAO
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListadoReportesViewModel(
    private val dao: ReporteDAO
) : ViewModel()
{
    private val db = FirebaseFirestore.getInstance()
    private val _state = mutableStateOf(ReportState())
    val state: ReportState
        get() = _state.value
    private val _fechasFiltro = MutableStateFlow<Pair<Date?, Date?>>(null to null)
    val fechasFiltro: StateFlow<Pair<Date?, Date?>> = _fechasFiltro
    private val _latitud = MutableStateFlow<Double?>(null)
    private val _longitud = MutableStateFlow<Double?>(null)
    val latitud = _latitud.asStateFlow()
    val longitud = _longitud.asStateFlow()
    fun setUbicacion(lat: Double, lng: Double){
        _latitud.value = lat
        _longitud.value = lng
    }
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
            reportFecha = state.reportDate,
            reportImagenUri = state.reportImagenUri,
            latitud = latitud.value,
            longitud = longitud.value
        )
        println("Fecha guardada: ${updatedReport.reportFecha}")///para ver en que formato se guarda la fecha cuando creo el reporte
        viewModelScope.launch {
            dao.insertReporte(updatedReport) //Actualiza la BD local
            uploadReporteToFirestore(updatedReport) //Actualiza la BD en la nube
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

    fun obtenerReportePorId(id: Int): Reporte? {
        return state.report?.find {it.reportId == id}
    }

    ////////////EDITAR REPORTE//////
    fun loadReport(reporte: Reporte) {
        _state.value = _state.value.copy(
            reportId = reporte.reportId,
            reportTitle = reporte.reportTitulo,
            reportDescription = reporte.reportDescripcion,
            reportDate = reporte.reportFecha,
            reportImagenUri = reporte.reportImagenUri,
            reportLat = reporte.latitud,
            reportLng = reporte.longitud
        )
    }

    fun updateReport() {
        val updatedReport = Reporte(
            reportId = state.reportId,
            reportTitulo = state.reportTitle,
            reportDescripcion = state.reportDescription,
            reportFecha = state.reportDate,
            reportImagenUri = state.reportImagenUri,
            latitud = state.reportLat,
            longitud = state.reportLng
        )
        viewModelScope.launch {
            dao.updateReporte(updatedReport) //Actualiza la BD local
            uploadReporteToFirestore(updatedReport)//Actualiza la BD en la nube
        }
        clearForm()
    }

    /////////////////FILTRADO DE FECHAS///////////////////////////////////
    private fun filterReportesByDates(
        reportes: List<Reporte>,
        fromDate: Date?,
        toDate: Date?
    ): List<Reporte> {
        //val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val from = fromDate?.onlyDate()
        val to = toDate?.onlyDate()

        return reportes.filter { reporte ->
            val fechaReporte: Date? = try {
                dateFormatter.parse(reporte.reportFecha)?.onlyDate()
            } catch (e: Exception) {
                null
            }
            fechaReporte != null &&
                    (from == null || !fechaReporte.before(from)) &&
                    (to == null || !fechaReporte.after(to))
        }
    }
    fun changeLocation (lat: Double, lng: Double){
        _state.value = _state.value.copy(
            reportLat = lat,
            reportLng = lng
        )
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

    fun uploadReporteToFirestore(reporte: Reporte) {
        val data = hashMapOf(
            "titulo" to reporte.reportTitulo,
            "descripcion" to reporte.reportDescripcion,
            "fecha" to reporte.reportFecha,
            "imagenUri" to (reporte.reportImagenUri ?: ""),
            "latitud" to (reporte.latitud ?: 0.0),
            "longitud" to (reporte.longitud ?: 0.0)
        )
        db.collection("reportes")
            .document(reporte.reportId.toString()) //usa el mismo ID del reporte
            .set(data)// reemplaza o crea seguún corresponda
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Reporte actualizado o creadp con ID: ${reporte.reportId}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al subir reporte", e)
            }
    }
}