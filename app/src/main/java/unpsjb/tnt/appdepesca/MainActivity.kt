package unpsjb.tnt.appdepesca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import unpsjb.tnt.appdepesca.Reglamentos.ReglamentoScreen
import unpsjb.tnt.appdepesca.Reglamentos.ReglamentosViewModel
import unpsjb.tnt.appdepesca.concursos.ConcursoScreen
import unpsjb.tnt.appdepesca.concursos.ConcursosViewModel
import unpsjb.tnt.appdepesca.database.PescaRoomDatabase
import unpsjb.tnt.appdepesca.listado.ListadoReportesScreen
import unpsjb.tnt.appdepesca.reporte.CrearReporteScreen
import unpsjb.tnt.appdepesca.reporte.ReporteViewModel
import unpsjb.tnt.appdepesca.login.LoginScreen
import unpsjb.tnt.appdepesca.login.LoginViewModel
import unpsjb.tnt.appdepesca.listado.ListadoReportesViewModel
import unpsjb.tnt.appdepesca.reporte.EditarReporteScreen
import unpsjb.tnt.appdepesca.ui.theme.ProyectoPesca2023Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            PescaRoomDatabase::class.java,
            "product_db3"
        ).build()
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
                        ReglamentoScreen(ReglamentosViewModel(), navController)
                    }
                    composable("concurso") {
                        ConcursoScreen(ConcursosViewModel(), navController)
                    }
                    composable("formulario") {
                        val reporteViewModel = ReporteViewModel()
                        CrearReporteScreen(reporteViewModel, listadoReportesViewModel, navController)
                    }
                    composable("editar_reporte") {
                        val reporteViewModel = ReporteViewModel()
                        EditarReporteScreen( reporteViewModel, listadoReportesViewModel, navController)
                    }
                }
            }
        }
    }
}