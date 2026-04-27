package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val correo: String, // Añadido como ejemplo de campos comunes
    val contrasena: String // Añadido como ejemplo de campos comunes
)
