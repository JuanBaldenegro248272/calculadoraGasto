package LosPrimos.Durango.calculadoragastos.model

import LosPrimos.Durango.calculadoragastos.model.enums.TipoPago
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "gastos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuarioPaga"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Grupo::class,
            parentColumns = ["idGrupo"],
            childColumns = ["idGrupo"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Tarjeta::class,
            parentColumns = ["idTarjeta"],
            childColumns = ["idTarjeta"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("idUsuarioPaga"),
        Index("idCategoria"),
        Index("idGrupo"),
        Index("idTarjeta")
    ]
)
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val idGasto: Int,
    val idUsuarioPaga: Int,
    val idCategoria: Int,
    val idGrupo: Int?,
    val idTarjeta: Int?,
    val monto: Double,
    val descripcion: String,
    val fecha: Long,
    val tipoPago: TipoPago,
    val lugar: String,
    val fotoRecibo: String?
)