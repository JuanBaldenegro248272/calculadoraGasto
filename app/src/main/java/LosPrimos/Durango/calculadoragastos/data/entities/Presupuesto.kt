package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "presupuestos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["idCategoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("idUsuario"),
        Index("idCategoria")
    ])
data class Presupuesto(
    @PrimaryKey(true)
    val idPresupuesto: Int,
    val monto: Double,
    val mes: Int,
    val anio: Int,
    val porcentaje: Int,
    val idUsuario: Int,
    val idCategoria: Int?
)