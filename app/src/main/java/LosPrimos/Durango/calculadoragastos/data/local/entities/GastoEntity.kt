package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "gastos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = TarjetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tarjetaId"],
            onDelete = ForeignKey.SET_NULL // Si borras la tarjeta, el gasto queda sin tarjeta asignada (efectivo)
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si borras al usuario, borras todos sus gastos
        ),
        ForeignKey(
            entity = GrupoEntity::class,
            parentColumns = ["id"],
            childColumns = ["grupoId"],
            onDelete = ForeignKey.SET_NULL // Si borras el grupo, el gasto no se borra, pero el grupoId pasa a null
        )
    ],
    indices = [
        Index("categoriaId"),
        Index("tarjetaId"),
        Index("usuarioId"),
        Index("grupoId")
    ]
)
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monto: Double,
    val fecha: Long,
    val descripcion: String? = null,
    val categoriaId: Long,
    val tarjetaId: Long? = null, // Puede ser efectivo, por lo que no es obligatoria una tarjeta
    val usuarioId: Long,
    val grupoId: Long? = null // Opcional, si es null es un gasto individual normal
)
