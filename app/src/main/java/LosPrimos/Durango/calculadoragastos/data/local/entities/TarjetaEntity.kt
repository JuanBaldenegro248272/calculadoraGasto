package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarjetas")
data class TarjetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String, // ej. "Débito BBVA", "Crédito Nu"
    val ultimosDigitos: String? = null // Útil para identificar la tarjeta en pantalla
)
