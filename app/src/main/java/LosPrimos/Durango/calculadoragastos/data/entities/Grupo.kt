package LosPrimos.Durango.calculadoragastos.data.entities


data class Grupo(
    val idGrupo: String = "",
    val nombre: String = "",
    val tipo: String = "",
    val imagenGrupo: String = "",
    val codigo: String = "",
    val idUsuario: Int? = 0,
    val miembros: List<Int?> = emptyList()
)