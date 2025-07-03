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
import unpsjb.tnt.appdepesca.formulario.CrearReporteScreen
import unpsjb.tnt.appdepesca.formulario.ReportesViewModel
import unpsjb.tnt.appdepesca.login.LoginScreen
import unpsjb.tnt.appdepesca.login.LoginViewModel
import unpsjb.tnt.appdepesca.reportes.ReportScreen
import unpsjb.tnt.appdepesca.reportes.ReportViewModel
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
                return ReportViewModel(dao, ReportesViewModel()) as T
            }
        }
        val reportViewModel: ReportViewModel by viewModels { viewModelFactory }

        setContent {
            ProyectoPesca2023Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(LoginViewModel(), navController)
                    }
                    composable("home") {
                        ReportScreen(reportViewModel, ReportesViewModel(), navController)
                    }
                    composable("reportes") {
                        ReportScreen(reportViewModel, ReportesViewModel(), navController)
                    }
                    composable("reglamentos") {
                        ReglamentoScreen(ReglamentosViewModel(), navController)
                    }
                    composable("concurso") {
                        ConcursoScreen(ConcursosViewModel(), navController)
                    }
                    composable("formulario") {
                        val reportesViewModel = ReportesViewModel()
                        CrearReporteScreen(reportesViewModel, reportViewModel, navController)
                    }
                }
            }
        }
    }
}