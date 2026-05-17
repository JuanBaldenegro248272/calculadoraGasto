package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GastoGrupo(
    val idGastoGrupo: String = "",
    val idGrupo: String = "",
    val idUsuarioPagador: String = "",
    val montoTotal: Double = 0.0,
    val idCategoria: Int = 0,
    val cantidadMiembros: Int = 1,
    val descripcion: String = "",
    val fecha: Long = 0L,
    val fotoRecibo: String? = null
)
