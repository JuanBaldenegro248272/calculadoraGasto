package LosPrimos.Durango.calculadoragastos.ui.screens

import LosPrimos.Durango.calculadoragastos.navigation.Screen
import LosPrimos.Durango.calculadoragastos.ui.components.AuthBackground
import LosPrimos.Durango.calculadoragastos.ui.components.ColorPink
import LosPrimos.Durango.calculadoragastos.ui.components.ColorTeal
import LosPrimos.Durango.calculadoragastos.ui.components.SpentButton
import LosPrimos.Durango.calculadoragastos.ui.components.SpentTextField
import LosPrimos.Durango.calculadoragastos.viewModel.AuthState
import LosPrimos.Durango.calculadoragastos.viewModel.AuthViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


private const val EDAD_MINIMA = 15
private val OPCIONES_GENERO = listOf("Masculino", "Femenino", "Otro")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {
    var nombre          by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var genero          by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var repeatPassword  by remember { mutableStateOf("") }

    var fechaNacimientoMs by remember { mutableStateOf<Long?>(null) }
    val fechaNacimientoTexto = fechaNacimientoMs?.let {
        val formateador = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formateador.timeZone = TimeZone.getTimeZone("UTC")
        formateador.format(Date(it))
    } ?: ""

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.YEAR, -EDAD_MINIMA)
                }
                return utcTimeMillis <= cal.timeInMillis
            }
        }
    )

    var dropdownExpandido by remember { mutableStateOf(false) }

    var errorNombre         by remember { mutableStateOf<String?>(null) }
    var errorEmail          by remember { mutableStateOf<String?>(null) }
    var errorFecha          by remember { mutableStateOf<String?>(null) }
    var errorGenero         by remember { mutableStateOf<String?>(null) }
    var errorPassword       by remember { mutableStateOf<String?>(null) }
    var errorRepeatPassword by remember { mutableStateOf<String?>(null) }

    val authState   by viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    fun validar(): Boolean {
        var valido = true

        // Nombre
        if (nombre.isBlank()) {
            errorNombre = "El nombre es obligatorio"; valido = false
        } else errorNombre = null

        // Email
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (email.isBlank()) {
            errorEmail = "El correo es obligatorio"; valido = false
        } else if (!emailRegex.matches(email.trim())) {
            errorEmail = "Formato de correo inválido"; valido = false
        } else errorEmail = null

        // Fecha de nacimiento
        if (fechaNacimientoMs == null) {
            errorFecha = "Selecciona tu fecha de nacimiento"; valido = false
        } else {
            val calMinima = Calendar.getInstance().apply { add(Calendar.YEAR, -EDAD_MINIMA) }
            if (fechaNacimientoMs!! > calMinima.timeInMillis) {
                errorFecha = "Debes tener al menos $EDAD_MINIMA años"; valido = false
            } else errorFecha = null
        }

        // Genero
        if (genero.isBlank()) {
            errorGenero = "Selecciona un género"; valido = false
        } else errorGenero = null

        // Contraseña
        if (password.length < 8) {
            errorPassword = "La contraseña debe tener al menos 8 caracteres"; valido = false
        } else errorPassword = null

        // Repetir contraseña
        if (repeatPassword.isBlank()) {
            errorRepeatPassword = "Repite la contraseña"; valido = false
        } else if (password != repeatPassword) {
            errorRepeatPassword = "Las contraseñas no coinciden"; valido = false
        } else errorRepeatPassword = null

        return valido
    }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                RegisterFieldItem(
                    label = "Nombre:",
                    value = nombre,
                    error = errorNombre,
                    onValueChange = { nombre = it; errorNombre = null }
                )

                RegisterFieldItem(
                    label = "Correo Electrónico:",
                    value = email,
                    error = errorEmail,
                    onValueChange = { email = it; errorEmail = null }
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Fecha de Nacimiento:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        SpentTextField(
                            value = fechaNacimientoTexto,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Seleccionar fecha",
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Abrir calendario",
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { mostrarDatePicker = true }
                        )
                    }

                    AnimatedVisibility(visible = errorFecha != null) {
                        Text(
                            text = errorFecha ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 2.dp, start = 4.dp)
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Género:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpandido,
                        onExpandedChange = { dropdownExpandido = it }
                    ) {
                        SpentTextField(
                            value = genero,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Seleccionar género",
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpandido,
                            onDismissRequest = { dropdownExpandido = false }
                        ) {
                            OPCIONES_GENERO.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        genero = opcion
                                        errorGenero = null
                                        dropdownExpandido = false
                                    }
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = errorGenero != null) {
                        Text(
                            text = errorGenero ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 2.dp, start = 4.dp)
                        )
                    }
                }

                // Contraseña
                RegisterFieldItem(
                    label = "Contraseña:",
                    value = password,
                    error = errorPassword,
                    isPassword = true,
                    onValueChange = { password = it; errorPassword = null }
                )

                // Repetir contraseña
                RegisterFieldItem(
                    label = "Repetir Contraseña:",
                    value = repeatPassword,
                    error = errorRepeatPassword,
                    isPassword = true,
                    onValueChange = { repeatPassword = it; errorRepeatPassword = null }
                )
            }

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            SpentButton(text = "Crear Cuenta") {
                if (validar()) {
                    viewModel.register(
                        Usuario(
                            idUsuario        = 0,
                            nombre           = nombre.trim(),
                            correo           = email.trim(),
                            hashContrasena   = password,
                            fechaNacimiento  = fechaNacimientoMs!!,
                            genero           = genero,
                            fotoPerfil       = null,
                            fechaRegistro    = System.currentTimeMillis()
                        )
                    )
                }
            }

            // Indicador de carga
            AnimatedVisibility(
                visible = authState is AuthState.Loading,
                enter = fadeIn(), exit = fadeOut()
            ) {
                CircularProgressIndicator(color = ColorTeal)
            }


            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes una cuenta? ", color = Color.Gray)
                Text(
                    text = "Iniciar Sesión",
                    color = ColorPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { ms ->
                        fechaNacimientoMs = ms
                        errorFecha = null
                    }
                    mostrarDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun RegisterFieldItem(
    label: String,
    value: String,
    error: String? = null,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        SpentTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "",
            isPassword = isPassword
        )
        AnimatedVisibility(visible = error != null, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp)
            )
        }
    }
}