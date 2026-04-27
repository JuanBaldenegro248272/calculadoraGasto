package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class GrupoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val descripcion: String? = null // El signo de interrogación '?' indica que puede ser nulo (opcional)
)
