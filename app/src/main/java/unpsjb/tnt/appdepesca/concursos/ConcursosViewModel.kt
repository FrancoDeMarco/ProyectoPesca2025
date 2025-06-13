package unpsjb.tnt.appdepesca.concursos

import androidx.lifecycle.ViewModel
import unpsjb.tnt.appdepesca.database.Concurso

class ConcursosViewModel : ViewModel() {
    val concursos: List<Concurso> = listOf(
        Concurso(
            1,
            "Fiesta del Pescador Deportivo",
            "Villa Gesell, Buenos Aires",
            "04/11/2023",
            "La \"Fiesta del Pescador Deportivo\" es un evento anual que celebra la pasión por la pesca deportiva en Villa Gesell, " +
                    "Buenos Aires. Es una competencia abierta a pescadores de todas las edades y niveles de experiencia. Los participantes " +
                    "disfrutarán de una jornada de pesca emocionante en las aguas de la zona, compitiendo por capturar las especies más grandes y " +
                    "pesadas.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n" +
                    "Se permite el uso de cualquier técnica de pesca y cebos.\n" +
                    "El ganador será determinado por el peso total de las piezas capturadas.\n",
            "El ganador del concurso recibirá un trofeo, un premio en efectivo " +
                    "de \$10,000 y un equipo de pesca de alta calidad."),

        Concurso(
            2,
            "15º Concurso de 6 horas a la pieza mayor de peso",
            "Balneario Reta, Buenos aires",
            "27/01/2023",
            "El \"15º Concurso de 6 horas a la pieza mayor de peso\" es un evento emocionante que se lleva a cabo en el " +
                    "hermoso balneario de Reta, Buenos Aires. Los pescadores competirán durante seis horas en busca de capturar la pieza más " +
                    "grande en peso.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n" +
                    "Se permite el uso de cualquier técnica de pesca y cebos.\n",
            "El ganador del concurso recibirá un trofeo, un premio en efectivo " +
                    "de \$5,000 y un equipo de pesca de calidad."),

        Concurso(
            3,
            "52º Edición de las 24 horas de la Corivna Negra",
            "Claromecó, Reta y Orense",
            "09/02/2023",
            "La \"52º Edición de las 24 horas de la Corvina Negra\" es uno de los concursos de pesca más destacados de la " +
                    "región. Durante un día completo, los pescadores se reúnen en Claromecó, Reta y Orense para competir en la captura de " +
                    "corvinas negras.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n" +
                    "Se permite el uso de cualquier técnica de pesca y cebos.\n",
            "El ganador del concurso recibirá un trofeo, un premio en efectivo de \$15,000 y " +
                    "un viaje de pesca todo incluido a un destino turístico de renombre."),

        Concurso(
            4,
            "2do Concurso de Pesca en Kayak en Orense",
            "Orense, Buenos aires",
            "10/03/2023",
            "El \"2do Concurso de Pesca en Kayak en Orense\" ofrece a los entusiastas de la pesca la oportunidad de " +
                    "disfrutar de una competencia única en la hermosa localidad de Orense. Los participantes utilizarán kayaks para " +
                    "explorar las aguas y competir en la captura de las piezas más grandes.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años que utilicen kayaks como medio de pesca.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n" +
                    "Se permite el uso de cualquier técnica de pesca y cebos.\n",
            "El ganador del concurso recibirá un trofeo, un premio en " +
                    "efectivo de \$7,500 y un kayak de pesca de alta gama."),

        Concurso(
            5,
            "Torneo Ciudad de Miramar a la variada de Mayor Peso",
            "Miramar, Buenos Aires",
            "17/03/2023",
            "El \"Torneo Ciudad de Miramar a la variada de Mayor Peso\" es un evento popular que reúne a pescadores de todo el " +
                    "país en la pintoresca ciudad de Miramar. Durante el torneo, los participantes tendrán la oportunidad de pescar una " +
                    "variedad de especies y competir por el título de la variada de mayor peso.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n" +
                    "Se permite el uso de cualquier técnica de pesca y cebos.\n",
            "El ganador del concurso recibirá un trofeo, un premio en " +
                    "efectivo de \$8,000 y un paquete de vacaciones para dos personas en un destino de pesca reconocido."),

        Concurso(
            6,
            "6 Horas Concurso de Pesca Pascual Taules",
            "Trelew, Chubut",
            "24/02/2023",
            "El \"6 Horas Concurso de Pesca Pascual Taules\" rinde homenaje a uno de los pescadores más destacados de " +
                    "Trelew, Chubut. Durante seis horas intensas, los participantes competirán por capturar la mayor cantidad y el peso " +
                    "total de las piezas en un ambiente de camaradería y entusiasmo. El concurso se lleva a cabo en un entorno natural " +
                    "impresionante y ofrece la oportunidad de disfrutar de la pesca en una de las regiones más hermosas de Argentina.",
            "El concurso está abierto a todos los pescadores deportivos mayores de 18 años.\n" +
                    "Cada participante deberá presentar su licencia de pesca vigente.\n",
            "El ganador del concurso recibirá un trofeo, un premio en efectivo de \$6,000 " +
                    "y un paquete de productos de pesca de primera calidad.")
    )
}