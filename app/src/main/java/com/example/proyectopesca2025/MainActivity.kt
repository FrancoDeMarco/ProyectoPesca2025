package com.example.proyectopesca2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.*
import androidx.room.Room
import com.example.proyectopesca2025.database.PescaRoomDatabase
import com.example.proyectopesca2025.database.ReporteDAO
import com.example.proyectopesca2025.formulario.FormularioScreen
import com.example.proyectopesca2025.formulario.FormularioViewModel
import com.example.proyectopesca2025.login.LoginScreen
import com.example.proyectopesca2025.login.LoginViewModel
import com.example.proyectopesca2025.Reglamentos.ReglamentoScreen
import com.example.proyectopesca2025.Reglamentos.ReglamentosViewModel
import com.example.proyectopesca2025.concursos.ConcursoScreen
import com.example.proyectopesca2025.concursos.ConcursosViewModel
import com.example.proyectopesca2025.reportes.ReportScreen
import com.example.proyectopesca2025.reportes.ReportViewModel
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
                return ReportViewModel(dao, FormularioViewModel()) as T
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
                        ReportScreen(reportViewModel, FormularioViewModel(), navController)
                    }
                    composable("reportes") {
                        ReportScreen(reportViewModel, FormularioViewModel(), navController)
                    }
                    composable("reglamentos") {
                        ReglamentoScreen(ReglamentosViewModel(), navController)
                    }
                    composable("concurso") {
                        ConcursoScreen(ConcursosViewModel(), navController)
                    }
                    composable("formulario") {
                        val formularioViewModel = FormularioViewModel()
                        FormularioScreen(formularioViewModel, reportViewModel, navController)
                    }
                }
            }
        }
    }
}
