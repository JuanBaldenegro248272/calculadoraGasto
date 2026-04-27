package LosPrimos.Durango.calculadoragastos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val idCategoria: Int,
    val nombre: String,
    val color: String?,
    val icono: String?
)