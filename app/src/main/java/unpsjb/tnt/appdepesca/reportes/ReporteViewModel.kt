package unpsjb.tnt.appdepesca.reportes


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.database.Reporte


class ReporteViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var nuevoReporte: Reporte = Reporte(
        reportId = 0,
        reportTitulo = "",
        reportDescripcion = "",
        reportFecha = ""
    )

    fun changeTitle(title: String) {
        nuevoReporte = nuevoReporte.copy(reportTitulo = title)
    }

    fun changeDescription(description: String) {
        nuevoReporte = nuevoReporte.copy(reportDescripcion = description)
    }

    fun changeDate(date: String) {
        nuevoReporte = nuevoReporte.copy(reportFecha = date)
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun createReport(dateState: String, listadoReportesViewModel: ListadoReportesViewModel) {
        viewModelScope.launch {
            setLoading(true)
            nuevoReporte = nuevoReporte.copy(reportFecha = dateState)
            listadoReportesViewModel.createReport()
            setLoading(false)
        }
    }
}



