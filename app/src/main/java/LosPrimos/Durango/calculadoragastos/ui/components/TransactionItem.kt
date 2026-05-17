package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.ui.theme.DarkGrayText
import LosPrimos.Durango.calculadoragastos.ui.theme.LightBlueGray
import LosPrimos.Durango.calculadoragastos.ui.theme.MagentaPink
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.utils.formatCurrency
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionItem(
    titulo: String?,
    fecha: String,
    monto: Double,
    isGasto: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(titulo ?:"", fontWeight = FontWeight.Bold, color = DarkGrayText, fontSize = 16.sp)
            Text(fecha, color = LightBlueGray, fontSize = 12.sp)
        }

        val colorMonto = if (isGasto) MagentaPink else TealDark
        val signo = if (isGasto) "-" else "+"
        Text(
            text = "$signo${formatCurrency(monto)}",
            fontWeight = FontWeight.ExtraBold,
            color = colorMonto,
            fontSize = 16.sp
        )
    }
}
