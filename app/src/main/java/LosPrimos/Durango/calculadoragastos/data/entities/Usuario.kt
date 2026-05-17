package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.Date

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val idUsuario: String = "",
    val nombre: String = "",
    val correo: String = "",
    val hashContrasena: String = "",
    val fechaNacimiento: Long = 0L,
    val genero: String = "",
    val fotoPerfil: String? = null,
    val fechaRegistro: Long = 0L
)