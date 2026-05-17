package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.GastoDao
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.GastoGrupo
import LosPrimos.Durango.calculadoragastos.data.enums.TipoPago
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GastoGrupoRepository(
    private val gastoDao: GastoDao
) {
    private val db = FirebaseFirestore.getInstance()

    fun obtenerGastosDeGrupo(idGrupo: String, onResult: (List<GastoGrupo>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("gastosGrupo")
            .whereEqualTo("idGrupo", idGrupo)
            .get()
            .addOnSuccessListener { snapshot ->
                val listaGastos = snapshot.toObjects(GastoGrupo::class.java)
                onResult(listaGastos.sortedByDescending { it.fecha })
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    fun registrarGastoEnGrupo(
        gastoGrupo: GastoGrupo,
        miParteDeLaDeuda: Double,
        idUsuarioActual: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("gastosGrupo").document(gastoGrupo.idGastoGrupo).set(gastoGrupo)
            .addOnSuccessListener {

                CoroutineScope(Dispatchers.IO).launch {
                    val gastoPersonal = Gasto(
                        idGasto = 0,
                        idUsuarioPaga = idUsuarioActual,
                        idCategoria = null,
                        idGrupo = gastoGrupo.idGrupo,
                        idGastoGrupo = gastoGrupo.idGastoGrupo,
                        idTarjeta = null,
                        monto = miParteDeLaDeuda,
                        descripcion = "Grupo: ${gastoGrupo.descripcion}",
                        fecha = gastoGrupo.fecha,
                        tipoPago = TipoPago.EFECTIVO,
                        lugar = "Grupo",
                        fotoRecibo = gastoGrupo.fotoRecibo,
                        esFijo = false
                    )
                    gastoDao.insertGasto(gastoPersonal)
                }

                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}