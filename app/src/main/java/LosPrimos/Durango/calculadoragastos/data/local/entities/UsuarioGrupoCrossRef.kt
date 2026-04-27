package LosPrimos.Durango.calculadoragastos.data.local.entities

import androidx.room.Entity
import androidx.room.Index

/**
 * Tabla intermedia para la relación de Muchos a Muchos (N:M) entre Usuarios y Grupos.
 * Un usuario puede estar en varios grupos, y un grupo tiene varios usuarios.
 */
@Entity(
    tableName = "usuario_grupo_cross_ref",
    primaryKeys = ["usuarioId", "grupoId"],
    indices = [Index("grupoId")]
)
data class UsuarioGrupoCrossRef(
    val usuarioId: Long,
    val grupoId: Long
)
