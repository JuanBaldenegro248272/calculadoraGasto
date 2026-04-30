package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "ingresos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idUsuario")]
)
data class Ingreso(
    @PrimaryKey(true)
    val idIngreso: Int,
    val monto: Double,
    val fecha: Long,
    val descripcion: String?,
    val idCategoria: Int,
    val idUsuario: Int
)