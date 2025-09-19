package unpsjb.tnt.appdepesca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
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
import unpsjb.tnt.appdepesca.reportes.ListadoReportesViewModel
import unpsjb.tnt.appdepesca.reportes.DetalleReporteScreen
import unpsjb.tnt.appdepesca.reportes.EditarReporteScreen
import unpsjb.tnt.appdepesca.ui.theme.ProyectoPesca2023Theme
import unpsjb.tnt.appdepesca.reglamentos.DetalleReglamentoScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                return ListadoReportesViewModel(dao) as T
            }
        }
        val listadoReportesViewModel: ListadoReportesViewModel by viewModels { viewModelFactory }
        setContent {
            ProyectoPesca2023Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(LoginViewModel(), navController)
                    }
                    composable("home") {
                        ListadoReportesScreen(listadoReportesViewModel, navController)
                    }
                    composable("reportes") {
                        ListadoReportesScreen(listadoReportesViewModel, navController)
                    }
                    composable("reglamentos") {
                        ListaReglamentosScreen(ReglamentosViewModel(), navController)
                    }
                    composable(
                        "detalleReglamento/{reglamentoId}",
                        arguments = listOf(navArgument("reglamentoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val reglamentoId = backStackEntry.arguments?.getInt("reglamentoId") ?: return@composable
                        DetalleReglamentoScreen(
                            reglamentoId = reglamentoId,
                            viewModel = ReglamentosViewModel(),
                            navController = navController
                        )
                    }
                    composable("concurso") {
                        ListaConcursosScreen(ConcursosViewModel(), navController)
                    }
                    composable (
                        "detalleconcurso/{concursoId}",
                        arguments = listOf(navArgument ("concursoId"){type = NavType.IntType})
                    ){ backStackEntry ->
                        val concursoId = backStackEntry.arguments?.getInt("concursoId") ?: return@composable
                        DetalleConcursoScreen(
                            concursoId = concursoId,
                            viewModel = ConcursosViewModel(),
                            navController = navController
                        )
                    }
                    composable("formulario") {
                        CrearReporteScreen(listadoReportesViewModel, navController)
                    }
                    composable("editar_reporte") {
                        val reporteViewModel = ReporteViewModel()
                        EditarReporteScreen( reporteViewModel, listadoReportesViewModel, navController)
                    }
                    composable(
                        "detalle_reporte/{reporteId}",
                        arguments = listOf(navArgument("reporteId") { type = NavType.IntType })
                    ) {
                        val reporteId = it.arguments?.getInt("reporteId") ?: return@composable
                        DetalleReporteScreen(
                            reporteId = reporteId,
                            navController = navController,
                            listadoReportesViewModel
                        )
                    }
                }
            }
        }
    }
}
