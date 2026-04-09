package LosPrimos.Durango.calculadoragastos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BalanceCard(gastado: Double, balance: Double){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC7E3FF)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            SummaryHeader("Resumen mensual")
            BalanceSection(gastado)
            BudgetSummary(balance, balance - gastado)
            ProgressBar(gastado, balance)
            Spacer(modifier = Modifier.height(20.dp))
            TransactionButtons {  }
        }
    }
}

@Composable
@Preview
fun BalanceCardPreview(){
    BalanceCard(23.00,2300.00)
}