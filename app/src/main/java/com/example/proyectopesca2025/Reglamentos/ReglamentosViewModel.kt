package com.example.proyectopesca2025.Reglamentos

import androidx.lifecycle.ViewModel
import com.example.proyectopesca2025.database.Reglamento

class ReglamentosViewModel : ViewModel() {
    val reglamentos: List<Reglamento> = listOf(

        Reglamento(1,
            "Tamaño mínimo de captura",
            "Villa Gesell, Buenos Aires",
        "15/04/2022",
        "Este reglamento establece el tamaño mínimo permitido para la captura de peces durante el concurso de pesca. " +
                "Los participantes deben asegurarse de que los peces que capturen cumplan con el tamaño mínimo especificado antes de " +
                "presentarlos para su pesaje.",
            listOf(
                "www.ejemplo.com/enlace1",
                "www.ejemplo.com/enlace2",
                "www.ejemplo.com/enlace3"
            )),

        Reglamento(2,
        "Límite de capturas por participante",
            "Balneario Reta, Buenos aires",
        "01/07/2021",
        "Este reglamento establece un límite máximo de capturas por participante durante el concurso de pesca. " +
                "Cada concursante solo puede presentar un número determinado de peces para su pesaje, lo que ayuda a mantener un " +
                "equilibrio en la competencia y proteger las poblaciones de peces.",
            listOf(
                "www.ejemplo.com/enlace1",
                "www.ejemplo.com/enlace2",
                "www.ejemplo.com/enlace3"
            ),
        ),

        Reglamento(3,
        "Uso de señuelos artificiales solamente",
            "Claromecó, Reta y Orense",
        "10/05/2023",
        "Este reglamento establece que solo se permitirá el uso de señuelos artificiales durante el concurso de " +
                "pesca. Se prohíbe el uso de cebos vivos, como gusanos o peces, para garantizar una competencia justa y preservar " +
                "el entorno acuático.",
            listOf(
                "www.ejemplo.com/enlace1",
                "www.ejemplo.com/enlace2",
                "www.ejemplo.com/enlace3"
            )
        ),

        Reglamento(4,
        "Zonas de pesca restrigidas",
            "Orense, Buenos aires",
        "20/03/2022",
        "Este reglamento establece las zonas de pesca restringidas durante el concurso. Algunas áreas pueden " +
                "estar prohibidas debido a la conservación de especies en peligro de extinción o la protección de hábitats frágiles. " +
                "Los participantes deben respetar estas restricciones y pescar solo en las zonas permitidas.",
            listOf(
                "www.ejemplo.com/enlace1",
                "www.ejemplo.com/enlace2",
                "www.ejemplo.com/enlace3"
            )
        ),

        Reglamento(5,
        "Prohibición de dispositivos de pesca automatizados",
            "Miramar, Buenos Aires",
        "05/02/2023",
        "Este reglamento prohíbe el uso de dispositivos de pesca automatizados, como redes o trampas, durante el " +
                "concurso de pesca. Se busca fomentar una pesca deportiva responsable y justa, promoviendo la participación activa de " +
                "los concursantes en la captura de los peces.",
            listOf(
                "www.ejemplo.com/enlace1",
                "www.ejemplo.com/enlace2",
                "www.ejemplo.com/enlace3"
            )
        ),

    )
}