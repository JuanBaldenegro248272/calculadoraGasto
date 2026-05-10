package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.data.repositories.UsuarioRepository
import LosPrimos.Durango.calculadoragastos.navigation.Screen
import LosPrimos.Durango.calculadoragastos.ui.components.AuthBackground
import LosPrimos.Durango.calculadoragastos.ui.components.ColorPink
import LosPrimos.Durango.calculadoragastos.ui.components.SpentButton
import LosPrimos.Durango.calculadoragastos.ui.components.SpentTextField
import LosPrimos.Durango.calculadoragastos.utils.Biometrics
import LosPrimos.Durango.calculadoragastos.viewModel.AuthState
import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import android.R.attr.password
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val usuarioRecordado by viewModel.usuarioRecordado.collectAsState()
    val authState by viewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    AuthBackground(isRememberedUser = usuarioRecordado != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("SPENT", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)

            if (usuarioRecordado == null) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(120.dp),
                    tint = Color.DarkGray
                )

                Text("Inicio De Sesion", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SpentTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Correo electrónico",
                        trailingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Color.Gray) }
                    )
                    SpentTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Contraseña",
                        isPassword = true,
                        trailingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray) }
                    )
                }

                SpentButton(text = "Iniciar sesión") {
                    viewModel.login(email, password)
                }

                Row {
                    Text("No tienes una cuenta? ", color = Color.Gray)
                    Text(
                        text = "Registrate",
                        color = ColorPink,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            onNavigateToRegister()
                        }
                    )
                }

            } else {
                val context = LocalContext.current
                val activity = context as FragmentActivity

                val Helper = remember(activity) {
                    Biometrics(
                        activity = activity,
                        onSuccess = {
                            usuarioRecordado?.idUsuario?.let { id ->
                                viewModel.loginConBiometria(id)
                            }
                        },
                        onError = { error ->
                        }
                    )
                }

                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(120.dp),
                    tint = Color.DarkGray
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Bienvenido, ${usuarioRecordado!!.nombre}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "¿No Eres ${usuarioRecordado!!.nombre}?",
                        color = ColorPink,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.desvincularCuenta() }
                    )
                }

                SpentTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Contraseña",
                    isPassword = true,
                    trailingIcon = {
                        Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray)
                    }
                )
                
                if (Helper.isDisponible()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { Helper.mostrarPrompt() }
                    ) {
                        Icon(
                            Icons.Default.Fingerprint,
                            contentDescription = "Huella",
                            tint = ColorPink,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Iniciar sesión con biometría",
                            color = ColorPink,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                SpentButton(text = "Iniciar sesión") {
                    viewModel.login(usuarioRecordado!!.correo, password)
                }
            }
        }
    }
}