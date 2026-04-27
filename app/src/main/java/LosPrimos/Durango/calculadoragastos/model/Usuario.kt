package LosPrimos.Durango.calculadoragastos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.Date

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val hashContrasena: String,
    val fechaNacimiento: Date,
    val genero: String,
    val fotoPerfil: String,
    val fechaRegistro: Timestamp
)