package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DetalleFila(etiqueta: String, valor: String, isMonto: Boolean = false, isGasto: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(
            text = valor,
            color = if (isMonto) { if (isGasto) MagentaPink else TealDark } else Color.Black,
            fontSize = if (isMonto) 18.sp else 14.sp,
            fontWeight = if (isMonto) FontWeight.ExtraBold else FontWeight.Normal
        )
    }
}