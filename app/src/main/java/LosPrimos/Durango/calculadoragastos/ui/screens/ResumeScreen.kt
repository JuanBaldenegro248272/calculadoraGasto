package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.model.Gasto
import LosPrimos.Durango.calculadoragastos.ui.components.BalanceCard
import LosPrimos.Durango.calculadoragastos.ui.components.BudgetSummary
import LosPrimos.Durango.calculadoragastos.ui.components.GastoRow
import LosPrimos.Durango.calculadoragastos.ui.components.MoreButton
import LosPrimos.Durango.calculadoragastos.ui.components.ProfileHeader
import LosPrimos.Durango.calculadoragastos.ui.components.SummaryHeader
import LosPrimos.Durango.calculadoragastos.ui.components.TransactionButtons
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResumeScreen(){

    val listaGastos = listOf<Gasto>(
        Gasto(1,"Marisquitos","Ayer",20.0,Icons.Default.Call),
        Gasto(2,"Party","Hoy",20.0,Icons.Default.Call),
        Gasto(3,"Shopping","Antier",20.0,Icons.Default.Call),
        Gasto(4,"Ropa","15 Abr",20.0,Icons.Default.Call)

    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item { ProfileHeader("Hector Garduno") }
        item {
            BalanceCard(
                gastado = 23.0,
                balance = 2300.0
            )
        }
        item {
            Text(
                "Movimientos recientes",
                modifier = Modifier
                    .padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        items(listaGastos.take(2)) { gasto ->
            GastoRow(
                nombre = gasto.nombre,
                fecha = gasto.fecha,
                monto = gasto.monto,
                icono = gasto.icono
            )
        }

        item { MoreButton {  } }
    }
}

@Composable
@Preview(showBackground = true)
fun ResumeScreenPreview(){
    ResumeScreen()
}