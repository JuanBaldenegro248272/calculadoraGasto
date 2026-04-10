package LosPrimos.Durango.calculadoragastos.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Gasto(
    val id: Int,
    val nombre: String,
    val fecha: String,
    val monto: Double,
    val icono: ImageVector
)