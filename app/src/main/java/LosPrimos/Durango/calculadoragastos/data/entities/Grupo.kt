package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "grupos")
data class Grupo(
    @PrimaryKey
    val idGrupo: String,
    val nombre: String,
    val tipo: String,
    val imagenGrupo: String,
    val codigo: String,
    val idUsuarioCreador: String,
    val miembrosIds: String // no se puede usar list en firebase, hay que separarlos como en , o algo asi
)