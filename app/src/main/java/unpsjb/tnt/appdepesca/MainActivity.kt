package unpsjb.tnt.appdepesca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.firebase.FirebaseApp
import unpsjb.tnt.appdepesca.reglamentos.ListaReglamentosScreen
import unpsjb.tnt.appdepesca.reglamentos.ReglamentosViewModel
import unpsjb.tnt.appdepesca.concursos.ListaConcursosScreen
import unpsjb.tnt.appdepesca.concursos.ConcursosViewModel
import unpsjb.tnt.appdepesca.concursos.DetalleConcursoScreen
import unpsjb.tnt.appdepesca.database.PescaRoomDatabase
import unpsjb.tnt.appdepesca.reportes.ListadoReportesScreen
import unpsjb.tnt.appdepesca.reportes.CrearReporteScreen
import unpsjb.tnt.appdepesca.reportes.ReporteViewModel
import unpsjb.tnt.appdepesca.login.LoginScreen
import unpsjb.tnt.appdepesca.login.LoginViewModel
import unpsjb.tnt.appdepesca.login.ResetPasswordScreen
import unpsjb.tnt.appdepesca.registro.RegistroScreen
import unpsjb.tnt.appdepesca.reportes.ListadoReportesViewModel
import unpsjb.tnt.appdepesca.reportes.DetalleReporteScreen
import unpsjb.tnt.appdepesca.reportes.EditarReporteScreen
import unpsjb.tnt.appdepesca.ui.theme.ProyectoPesca2023Theme
import unpsjb.tnt.appdepesca.reglamentos.DetalleReglamentoScreen
import unpsjb.tnt.appdepesca.reportes.MapaReportesScreen
import unpsjb.tnt.appdepesca.reportes.SeleccionarUbicacionCrearScreen
import unpsjb.tnt.appdepesca.reportes.SeleccionarUbicacionEditarScreen
import unpsjb.tnt.appdepesca.ui.components.LayoutBase
import unpsjb.tnt.appdepesca.usuario.UsuarioViewModel

class MainActivity : ComponentActivity() {
    val usuarioViewModel: UsuarioViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val database = Room.databaseBuilder(
            applicationContext,
            PescaRoomDatabase::class.java,
            "product_db3"
        )
            .fallbackToDestructiveMigration() //Esta línea destruye la DB si el esquema cambia
            .build()

        val dao = database.pescaDAO

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ListadoReportesViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ListadoReportesViewModel(dao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
            }
        }

        val listadoReportesViewModel: ListadoReportesViewModel by viewModels { viewModelFactory }

        setContent {
            ProyectoPesca2023Theme {
                AppNavigation(usuarioViewModel, listadoReportesViewModel)

            }
        }
    }

    @Composable
    fun AppNavigation(
        usuarioVM: UsuarioViewModel,
        listadoReportesVM: ListadoReportesViewModel
    ){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                val vm: LoginViewModel = viewModel()
                LoginScreen(vm, navController, usuarioViewModel)

            }
            composable("home") {
                LayoutBase(usuarioVM) {
                    ListadoReportesScreen(listadoReportesVM, navController)
                }
            }
            composable("reportes") {
                LayoutBase(usuarioVM) {
                    ListadoReportesScreen(listadoReportesVM, navController)
                }
            }
            composable("reglamentos") {
                val reglamentosViewModel: ReglamentosViewModel = viewModel()
                LayoutBase(usuarioVM) {
                    ListaReglamentosScreen(reglamentosViewModel, navController)
                }
            }
            composable(
                "detalleReglamento/{reglamentoId}",
                arguments = listOf(navArgument("reglamentoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val reglamentoId =
                    backStackEntry.arguments?.getInt("reglamentoId") ?: return@composable
                val reglamentosViewModel: ReglamentosViewModel = viewModel()
                LayoutBase(usuarioVM) {
                    DetalleReglamentoScreen(
                        reglamentoId = reglamentoId,
                        viewModel = reglamentosViewModel,
                        navController = navController
                    )
                }
            }
            composable("concurso") {
                val concursosViewModel: ConcursosViewModel = viewModel()
                LayoutBase(usuarioVM) {
                    ListaConcursosScreen(concursosViewModel, navController)
                }
            }
            composable("detalleconcurso/{concursoId}",
                arguments = listOf(navArgument("concursoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val concursoId = backStackEntry.arguments?.getInt("concursoId") ?: return@composable
                val concursosViewModel: ConcursosViewModel = viewModel()
                LayoutBase(usuarioVM) {
                    DetalleConcursoScreen(
                        concursoId = concursoId,
                        viewModel = concursosViewModel,
                        navController = navController
                    )
                }
            }

            composable("formulario") {
                LayoutBase(usuarioVM) {
                    CrearReporteScreen(listadoReportesVM, navController)
                }
            }
            composable(
                "editar_reporte/{reporteId}",
                arguments = listOf(navArgument("reporteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val reporteId =
                    backStackEntry.arguments?.getInt("reporteId") ?: return@composable
                val reporteViewModel: ReporteViewModel = viewModel()
                // Cargar reporte actual en el ViewModel antes de mostrar la pantalla
                LaunchedEffect(reporteId) {
                    val reporte = listadoReportesVM.obtenerReportePorId(reporteId)
                    if (reporte != null) {
                        listadoReportesVM.loadReport(reporte)
                    }
                }
                LayoutBase(usuarioVM) {
                    EditarReporteScreen(
                        reporteViewModel = reporteViewModel,
                        listadoReportesViewModel = listadoReportesVM,
                        navController = navController
                    )
                }
            }
            composable(
                "detalle_reporte/{reporteId}",
                arguments = listOf(navArgument("reporteId") { type = NavType.IntType })
            ) {
                val reporteId = it.arguments?.getInt("reporteId") ?: return@composable
                LayoutBase(usuarioVM) {
                    DetalleReporteScreen(
                        reporteId = reporteId,
                        navController = navController,
                        listadoReportesViewModel = listadoReportesVM
                    )
                }

            }
            composable("mapa_reportes") {
                LayoutBase(usuarioVM) {
                    MapaReportesScreen(
                        listadoReportesViewModel = listadoReportesVM,
                        navController = navController
                    )
                }
            }

            composable("seleccionar_ubicacion_crear") {
                LayoutBase(usuarioVM){
                    SeleccionarUbicacionCrearScreen(
                        listadoReportesViewModel = listadoReportesVM,
                        navController = navController
                    )
                }
            }
            composable("seleccionar_ubicacion_editar") {
                LayoutBase(usuarioVM) {
                    SeleccionarUbicacionEditarScreen(
                        listadoReportesViewModel = listadoReportesVM,
                        navController = navController
                    )
                }
            }
            composable("registro"){
                RegistroScreen(navController)
            }
            composable("resetPassword") {
                ResetPasswordScreen(navController)
            }
        }
    }
}
