package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import com.google.firebase.firestore.FirebaseFirestore


class GrupoRepository {

    private val db = FirebaseFirestore.getInstance()

    fun crearGrupo(grupo: Grupo, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("grupos").document(grupo.idGrupo).set(grupo)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun obtenerGrupos(usuarioId: String, onResult: (List<Grupo>) -> Unit, onError: (Exception) -> Unit) {
        db.collection("grupos")
            .whereArrayContains("miembrosIds", usuarioId)
            .get()
            .addOnSuccessListener { snapshot ->
                val grupos = snapshot.toObjects(Grupo::class.java)
                onResult(grupos)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun unirseAGrupoPorCodigo(codigo: String, usuarioId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("grupos")
            .whereEqualTo("codigo", codigo)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val documento = snapshot.documents.first()
                    val grupo = documento.toObject(Grupo::class.java)

                    if (grupo != null && !grupo.miembrosIds.contains(usuarioId)) {
                        val nuevaLista = grupo.miembrosIds.toMutableList()
                        nuevaLista.add(usuarioId)

                        db.collection("grupos").document(grupo.idGrupo)
                            .update("miembrosIds", nuevaLista)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onError(it) }
                    } else {
                        onError(Exception("Ya eres miembro de este grupo o el grupo no es válido."))
                    }
                } else {
                    onError(Exception("No se encontró ningún grupo con ese código."))
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}