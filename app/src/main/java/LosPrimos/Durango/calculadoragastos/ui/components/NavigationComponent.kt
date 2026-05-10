package LosPrimos.Durango.calculadoragastos.ui.components

import LosPrimos.Durango.calculadoragastos.ui.theme.DarkGrayText
import LosPrimos.Durango.calculadoragastos.ui.theme.LightBlueGray
import LosPrimos.Durango.calculadoragastos.ui.theme.SurfaceWhite
import LosPrimos.Durango.calculadoragastos.ui.theme.TealDark
import LosPrimos.Durango.calculadoragastos.ui.theme.TealLight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun SpentBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Inicio", "home", Icons.Default.Home),
        BottomNavItem("Presupuesto", "presupuestos", Icons.Default.AccountBalanceWallet),
        BottomNavItem("Grupos", "grupos", Icons.Default.Group),
        BottomNavItem("Gráficas", "graficas", Icons.Default.PieChart),
        BottomNavItem("Perfil", "perfil", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = SurfaceWhite,
        contentColor = DarkGrayText,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealDark,
                    selectedTextColor = TealDark,
                    indicatorColor = TealLight.copy(alpha = 0.2f),
                    unselectedIconColor = LightBlueGray,
                    unselectedTextColor = LightBlueGray
                )
            )
        }
    }
}