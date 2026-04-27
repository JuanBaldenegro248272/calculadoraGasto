package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "miembrosGrupo",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["idGrupo"],
            childColumns = ["idGrupo"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class MiembrosGrupo(
    val idUsuario: Int,
    val idGrupo: Int,
    val saldoPendiente: Double
)