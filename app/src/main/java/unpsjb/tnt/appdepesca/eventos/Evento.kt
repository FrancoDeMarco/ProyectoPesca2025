package unpsjb.tnt.appdepesca.eventos

data class Evento(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",// "concurso", "reglamento", "sorteo", "aviso"
    val lugar: String? = null,
    val fecha: String? = null,
    val bases: String? = null,
    val premio: String? = null,
    val enlaces: List<String>? = emptyList()
)
