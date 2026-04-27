package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ingresos")
data class Ingreso(
    @PrimaryKey(true)
    val idIngreso: Int,
    val monto: Double,
    val fecha: Date,
    val descripcion: String?,
    val idCategoria: Int
    )