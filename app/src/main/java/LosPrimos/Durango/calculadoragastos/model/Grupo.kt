package LosPrimos.Durango.calculadoragastos.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "grupos",
foreignKeys = [
    ForeignKey(
        entity = Usuario::class,
        parentColumns = ["idUsuario"],
        childColumns = ["idUsuario"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [
        Index("idUsuario")
    ]
)
data class Grupo(
    @PrimaryKey(autoGenerate = true)
    val idGrupo: Int,
    val nombre: String,
    val tipo: String,
    val imagenGrupo: String,
    val codigo: String,
    val idUsuario: Int
)