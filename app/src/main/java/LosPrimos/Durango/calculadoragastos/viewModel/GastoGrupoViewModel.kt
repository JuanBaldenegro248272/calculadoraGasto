package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.entities.GastoGrupo
import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
class GastoGrupoViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val usuarioActualId: StateFlow<String?> = MutableStateFlow(auth.currentUser?.uid)

    private val _gastosGrupo = mutableStateListOf<GastoGrupo>()
    val gastosGrupo: List<GastoGrupo> get() = _gastosGrupo

    private val _nombresUsuarios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nombresUsuarios: StateFlow<Map<String, String>> = _nombresUsuarios

    private val _totalesPorGrupo = MutableStateFlow<Map<String, Double>>(emptyMap())
    val totalesPorGrupo: StateFlow<Map<String, Double>> = _totalesPorGrupo

    private val _deudasPorGrupo = MutableStateFlow<Map<String, Double>>(emptyMap())
    val deudasPorGrupo: StateFlow<Map<String, Double>> = _deudasPorGrupo

    private var listenerRegistration: ListenerRegistration? = null

    fun obtenerGastos(idGrupo: String) {
        listenerRegistration?.remove()
        listenerRegistration = db.collection("gastosGrupo")
            .whereEqualTo("idGrupo", idGrupo)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    val gastos = it.toObjects(GastoGrupo::class.java)
                    _gastosGrupo.clear()
                    _gastosGrupo.addAll(gastos)
                    val idsNuevos = gastos.map { g -> g.idUsuarioPagador }.distinct()
                        .filter { id -> !_nombresUsuarios.value.containsKey(id) }
                    cargarNombresUsuarios(idsNuevos)
                }
            }
    }

    private fun cargarNombresUsuarios(ids: List<String>) {
        if (ids.isEmpty()) return
        val nombres = _nombresUsuarios.value.toMutableMap()
        ids.forEach { id ->
            db.collection("usuarios").document(id).get()
                .addOnSuccessListener { doc ->
                    nombres[id] = doc.getString("nombre") ?: "Usuario"
                    _nombresUsuarios.value = nombres.toMap()
                }
        }
    }

    fun agregarGasto(gasto: GastoGrupo) {
        val idNuevo = UUID.randomUUID().toString()
        db.collection("grupos").document(gasto.idGrupo).get()
            .addOnSuccessListener { documento ->
                val miembros = documento.get("miembrosIds") as? List<*>
                val cantidadMiembros = miembros?.size ?: 1
                val gastoConId = gasto.copy(
                    idGastoGrupo = idNuevo,
                    cantidadMiembros = cantidadMiembros
                )
                db.collection("gastosGrupo").document(idNuevo).set(gastoConId)
            }
    }

    fun actualizarGasto(gasto: GastoGrupo) {
        db.collection("gastosGrupo").document(gasto.idGastoGrupo).set(gasto)
    }

    fun eliminarGasto(gasto: GastoGrupo) {
        db.collection("gastosGrupo").document(gasto.idGastoGrupo).delete()
    }

    fun cargarResumenesDeGrupos(grupos: List<Grupo>, usuarioId: String) {
        val nuevosTotales = mutableMapOf<String, Double>()
        val nuevasDeudas = mutableMapOf<String, Double>()

        grupos.forEach { grupo ->
            db.collection("gastosGrupo")
                .whereEqualTo("idGrupo", grupo.idGrupo)
                .get()
                .addOnSuccessListener { snapshot ->
                    val gastos = snapshot.toObjects(GastoGrupo::class.java)
                    val total = gastos.sumOf { it.montoTotal }
                    val miParte = gastos.sumOf { gasto ->
                        val miembros = if (gasto.cantidadMiembros <= 0) 1 else gasto.cantidadMiembros
                        gasto.montoTotal / miembros
                    }

                    nuevosTotales[grupo.idGrupo] = total
                    nuevasDeudas[grupo.idGrupo] = miParte

                    _totalesPorGrupo.value = nuevosTotales.toMap()
                    _deudasPorGrupo.value = nuevasDeudas.toMap()
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
