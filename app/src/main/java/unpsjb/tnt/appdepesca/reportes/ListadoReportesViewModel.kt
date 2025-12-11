package unpsjb.tnt.appdepesca.reportes

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import unpsjb.tnt.appdepesca.database.Reporte
import unpsjb.tnt.appdepesca.database.ReporteDAO
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

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
    private val storage = FirebaseStorage.getInstance().reference
    fun setUbicacion(lat: Double, lng: Double){
        _latitud.value = lat
        _longitud.value = lng
    }
    private val _imagenSeleccionada = mutableStateOf<String?>(null)
    val imagenSeleccionada: State<String?> = _imagenSeleccionada
    private val _ordenDesc = MutableStateFlow(true)
    val ordenDesc: StateFlow<Boolean> = _ordenDesc
    private val _limite = MutableStateFlow(20)
    val limite: StateFlow<Int> = _limite.asStateFlow()

    fun setImagenSeleccionada(uri: String?){
        _imagenSeleccionada.value = uri
    }
    private val _uid = MutableStateFlow<String?>(null)
    val uid: StateFlow<String?> = _uid

    ///////////////CORAZÓN DEL VIEWMODEL/////////////////////////
    init {
        val auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _uid.value = user?.uid
        }
        viewModelScope.launch {
            combine(
                uid,
                fechasFiltro,
                ordenDesc
            ) { uidValue, fechas, desc -> Triple(uidValue, fechas, desc) }
                .collectLatest { (uidValue, fechas, desc) ->
                    if (uidValue == null) return@collectLatest //aún no sabemos qué usuario está logueado
                    syncFromFirestore(uidValue)
                    val flowBase = if (desc)
                        dao.getReportesByUsuarioOrdenadosPorFechaDesc(uidValue)
                    else
                        dao.getReportesByUsuarioOrdenadosPorFechaAsc(uidValue)

                    flowBase
                        .combine(fechasFiltro) { reportes, fechas ->
                            filterReportesByDates(
                                reportes,
                                fechas.first,
                                fechas.second
                            )
                        }
                        .collectLatest { reportesFiltrados -> // Actualiza el estado con los reportes filtrados
                            _state.value = state.copy(report = reportesFiltrados.take(_limite.value))// solo muestra 20 reportes por defecto
                        }
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
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val title = state.reportTitle
        val description= state.reportDescription
        val date = state.reportDate
        val localImagePath = state.reportImagenUri
        val lat = latitud.value
        val lng = longitud.value


        viewModelScope.launch {

            val uploadedUrl = localImagePath?.let { uploadImageToFirebase(it)} // Sube la imagen a Firebase Storage

            val updatedReport = Reporte(
                reportId = state.reportId, // Room lo genera
                reportTitulo = title,
                reportDescripcion = description,
                reportFecha = date,
                reportImagenUri = uploadedUrl, // guarda la URL de Firebase
                latitud = lat,
                longitud = lng,
                usuarioId = uid
            )
            println("Fecha guardada: ${updatedReport.reportFecha}")///para ver en que formato se guarda la fecha cuando creo el reporte

            try{
                val id = dao.insertReporte(updatedReport).toInt() //Crea el reporte en la BD local

                uploadReporteToFirestore(updatedReport.copy(reportId = id)) //Actualiza la BD en la nube

                clearForm() //Si salió bien, limpia el formulario
                println("Reporte creado correctamente.")

            }catch(e: Exception){
                Log.e("createReport", "Error al crear el reporte", e)// si hay algún error no se limpia el formulario
            }
        }
    }


    fun clearForm() {
        _state.value = _state.value.copy(
            reportId = 0,
            reportTitle = "",
            reportDescription = "",
            reportDate = "",
            reportImagenUri = null,
            reportLat = null,
            reportLng = null
        )
        limpiarImagenSeleccionada()
    }

    //para agregar la imagen
    fun changeImage(context: Context, uri: Uri) {
    val rutaInterna = guardarImagenEnInterno(context,uri)
        _state.value = state.copy(reportImagenUri = rutaInterna)
    }

    /////////////////////ELIMINAR REPORTE///////////////////
    fun deleteReporte(reporte: Reporte) {
        viewModelScope.launch {
            dao.deleteReporte(reporte) // elimina el reporte de la BD local
            deleteReporteFromFirestore(reporte.reportId) // elimina el reporte de la BD en la nube
        }
    }

    fun obtenerReportePorId(id: Int): Reporte? {
        return state.report.find {it.reportId == id}
    }

    ////////////EDITAR REPORTE//////
    fun loadReport(reporte: Reporte) {
        _state.value = _state.value.copy(
            reportId = reporte.reportId,
            reportTitle = reporte.reportTitulo,
            reportDescription = reporte.reportDescripcion,
            reportDate = reporte.reportFecha.toString(),
            reportImagenUri = reporte.reportImagenUri,
            reportLat = reporte.latitud,
            reportLng = reporte.longitud
        )
    }

    fun updateReport() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val updatedReport = Reporte(
            reportId = state.reportId,
            reportTitulo = state.reportTitle,
            reportDescripcion = state.reportDescription,
            reportFecha = state.reportDate,
            reportImagenUri = state.reportImagenUri,
            latitud = state.reportLat,
            longitud = state.reportLng,
            usuarioId = uid
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
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val from = fromDate?.onlyDate()
        val to = toDate?.onlyDate()

        return reportes.filter { reporte ->
            val fechaReporte: Date? = try {
                dateFormatter.parse(reporte.reportFecha)?.onlyDate()
            } catch (_: Exception) {
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

    /////////ACTUALIZA A LA BASE DE LA NUBE///////////
    fun uploadReporteToFirestore(reporte: Reporte) {
        val data = hashMapOf(
            "titulo" to reporte.reportTitulo,
            "descripcion" to reporte.reportDescripcion,
            "fecha" to reporte.reportFecha,
            "imagenUri" to (reporte.reportImagenUri ?: ""),
            "latitud" to (reporte.latitud ?: 0.0),
            "longitud" to (reporte.longitud ?: 0.0),
            "usuarioId" to reporte.usuarioId
        )
        db.collection("reportes")
            .document(reporte.reportId.toString()) //usa el mismo ID del reporte
            .set(data)// reemplaza o crea según corresponda
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Reporte actualizado o creado con ID: ${reporte.reportId}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al subir reporte", e)
            }
    }

    /////////ACTUALIZA A LA BASE DE LA NUBE///////////
    fun deleteReporteFromFirestore(reportId: Int){
        db.collection("reportes")
            .document(reportId.toString()) // usa el mismo ID del reporte
            .delete()// elimina el reporte de la BD en la nube
            .addOnSuccessListener {
                Log.d("Firestore", "Reporte eliminado con ID: $reportId")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al eliminar reporte", e)
            }
    }

    /////////GUARDA LA IMAGEN EN EL ALMACENAMIENTO INTERNO///////////
    fun guardarImagenEnInterno(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val nombreArchivo = "reporte_${System.currentTimeMillis()}.jpg"
            val archivoDestino = File(context.filesDir, nombreArchivo)
            val outputStream = FileOutputStream(archivoDestino)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            archivoDestino.absolutePath // Ruta persistente
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    fun limpiarImagenSeleccionada(){
        _imagenSeleccionada.value = null
    }

    suspend fun uploadImageToFirebase(localpath: String): String? {
        return try {
            val fileUri = Uri.fromFile(File(localpath))
            val imageRef = storage.child("reportes/${fileUri.lastPathSegment}")
            imageRef.putFile(fileUri).await() //Esperar a que la carga termine
            imageRef.downloadUrl.await().toString() // Luego obtenemos la URL pública
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /////////SINCRONIZA CON LA BASE DE DATOS EN LA NUBE///////////
    private fun syncFromFirestore(uid: String){
        db.collection("reportes")
            .whereEqualTo("usuarioId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreSync", "Error escuchando Firestore", error)
                    return@addSnapshotListener
                }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Reporte(
                            reportId = doc.id.toInt(),
                            reportTitulo = doc.getString("titulo") ?: "",
                            reportDescripcion = doc.getString("descripcion") ?: "",
                            reportFecha = doc.getString("fecha") ?: "",
                            reportImagenUri = doc.getString("imagenUri"),
                            latitud = doc.getDouble("latitud"),
                            longitud = doc.getDouble("longitud"),
                            usuarioId = doc.getString("usuarioId") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }

                } ?: emptyList()
                // Actualiza Room con esta lista
                viewModelScope.launch {
                    dao.replaceAll(lista)
                }
            }
    }

    /////////Para cambiar el orden de la lista///////////////
    fun toggleOrden(){
        _ordenDesc.value = !_ordenDesc.value
    }

    /////////PARA CARGAR MÁS REPORTES///////////////
    fun cargarMas(){
        _limite.value += 6
    }
}
