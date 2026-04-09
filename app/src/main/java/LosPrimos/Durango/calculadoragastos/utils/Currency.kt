package LosPrimos.Durango.calculadoragastos.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String{
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}