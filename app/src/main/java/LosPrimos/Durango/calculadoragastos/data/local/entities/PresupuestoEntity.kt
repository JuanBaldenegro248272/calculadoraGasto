package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "presupuestos",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si se borra el usuario, se borran sus presupuestos
        )
    ],
    indices = [Index("usuarioId")] // Importante: Room recomienda crear índices para las llaves foráneas para mejorar la velocidad de consulta
)
data class PresupuestoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monto: Double,
    val mes: Int, // Ej. 1 para Enero
    val anio: Int, // Ej. 2026
    val usuarioId: Long // Esta es nuestra Foreign Key que conecta con el Usuario
)
