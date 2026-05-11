package LosPrimos.Durango.calculadoragastos.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import java.util.Locale

@SuppressLint("MissingPermission")
fun obtenerUbicacionGPS(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onUbicacionObtenida: (String) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                        val address = addresses.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
                        onUbicacionObtenida(address)
                    }
                } else {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
                    onUbicacionObtenida(address)
                }
            } catch (e: Exception) {
                onUbicacionObtenida("${location.latitude}, ${location.longitude}")
            }
        } else {
            onUbicacionObtenida("No se pudo obtener GPS (Enciende tu ubicación)")
        }
    }.addOnFailureListener {
        onUbicacionObtenida("Error al buscar GPS")
    }
}