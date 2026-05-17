package LosPrimos.Durango.calculadoragastos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tarjetas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("idUsuario")
    ])
data class Tarjeta(
    @PrimaryKey(autoGenerate = true) val idTarjeta: Int,
    val nombreTarjeta: String,
    val numeroTarjeta: String,
    val ultimoCuatro: String,
    val idUsuario: String
)