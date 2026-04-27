package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.Index

/**
 * Tabla intermedia para la relación de Muchos a Muchos (N:M) entre Usuarios y Categorías.
 * Representa que un usuario puede tener/crear varias categorías, y una categoría podría 
 * ser compartida por varios usuarios.
 */
@Entity(
    tableName = "categorias_usuario",
    primaryKeys = ["usuarioId", "categoriaId"], // La llave primaria es la combinación de ambos IDs
    indices = [Index("categoriaId")]
)
data class CategoriasUsuario(
    val usuarioId: Long,
    val categoriaId: Long
)
