package unpsjb.tnt.appdepesca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import unpsjb.tnt.appdepesca.formulario.FormularioScreen
import unpsjb.tnt.appdepesca.login.LoginScreen
import unpsjb.tnt.appdepesca.login.LoginViewModel
import unpsjb.tnt.appdepesca.ui.theme.ProyectoPesca2023Theme
import unpsjb.tnt.appdepesca.formulario.FormularioViewModel
import unpsjb.tnt.appdepesca.reportes.ReportScreen
import unpsjb.tnt.appdepesca.reportes.ReportViewModel
import unpsjb.tnt.appdepesca.database.PescaRoomDatabase
import unpsjb.tnt.appdepesca.database.ReporteDAO


class MainActivity : ComponentActivity() {
    private lateinit var dao: ReporteDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoPesca2023Theme {
                val database = Room.databaseBuilder(this, PescaRoomDatabase::class.java, "product_db3")
                    .build()
                dao = database.pescaDAO

                val viewModelFactory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ReportViewModel(dao, FormularioViewModel()) as T
                    }
                }
                val reportViewModel: ReportViewModel by viewModels(factoryProducer = { viewModelFactory })

                MyApp(modifier = Modifier.fillMaxSize(), reportViewModel, dao)
            }
        }
    }
}//

@Composable
fun MyApp(modifier: Modifier = Modifier, reportViewModel: ReportViewModel, dao: ReporteDAO) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {

        composable("reportes") { ReportScreen(ReportViewModel(dao, FormularioViewModel()), FormularioViewModel(), navController) }

        composable("reglamentos"){ ReglamentoScreen(ReglamentosViewModel(), navController)}

        composable("login") { LoginScreen(LoginViewModel(), navController) }

        composable("home") { ReportScreen(reportViewModel, FormularioViewModel(), navController) }

        composable("concurso") { ConcursoScreen(ConcursosViewModel(), navController) }

        //composable(("detalle2/{concursoId}")) { ConcursoDetalleScreen(ConcursosViewModel(), navController) }

        //composable(("detalle/{reglamentoId}")) { ReglamentoDetalleScreen(ReglamentosViewModel(), navController) }

        composable("formulario") {
            val formularioViewModel = remember { FormularioViewModel() }
            FormularioScreen(formularioViewModel, reportViewModel, navController)
        }
    }
}