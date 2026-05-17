package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Grupo(
    val idGrupo: String = "",
    val nombre: String = "",
    val tipo: String = "",
    val imagenGrupo: String = "",
    val codigo: String = "",
    val idUsuarioCreador: String = "",
    val miembrosIds: List<String> = emptyList()
)