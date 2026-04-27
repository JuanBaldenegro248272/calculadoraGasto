package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingresos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.RESTRICT // RESTRICT: Impide borrar una categoría si ya tiene ingresos asignados
        )
    ],
    indices = [Index("categoriaId")]
)
data class IngresoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monto: Double,
    val fecha: Long, // Guardaremos la fecha en formato "milisegundos" (timestamp)
    val descripcion: String? = null,
    val categoriaId: Long // Llave Foránea hacia CategoriaEntity
)
