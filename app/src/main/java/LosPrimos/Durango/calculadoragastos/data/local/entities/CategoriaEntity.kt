package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val color: String? = null, // Color opcional para identificar la categoría en la UI (ej. "#FF0000")
    val icono: String? = null // Icono opcional para la UI
)
