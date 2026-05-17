package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage


class GrupoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun crearGrupo(grupo: Grupo, imagenUri: Uri?, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        if (imagenUri != null) {
            val imagenReferencia = storage.reference.child("imagenesGrupos/${grupo.idGrupo}.jpg")

            imagenReferencia.putFile(imagenUri)
                .addOnSuccessListener {
                    imagenReferencia.downloadUrl
                        .addOnSuccessListener { url ->
                            val grupoConImagen = grupo.copy(imagenGrupo = url.toString())
                            guardarGrupo(grupoConImagen, onSuccess, onError)
                        }
                        .addOnFailureListener { exception ->
                            onError(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } else {
            guardarGrupo(grupo, onSuccess, onError)
        }
    }

    private fun guardarGrupo(grupo: Grupo, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
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

    fun obtenerGrupoPorId(grupoId: String, onResult: (Grupo?) -> Unit, onError: (Exception) -> Unit) {
        db.collection("grupos").document(grupoId)
            .get()
            .addOnSuccessListener { documento ->
                val grupo = documento.toObject(Grupo::class.java)
                onResult(grupo)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun unirseAGrupoFlexible(codigo: String, usuarioId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val codigoLimpio = codigo.trim().uppercase()

        db.collection("grupos")
            .whereEqualTo("codigo", codigoLimpio)
            .get()
            .addOnSuccessListener { resultadoTexto ->
                if (!resultadoTexto.isEmpty) {
                    agregarUsuarioAlGrupo(resultadoTexto, usuarioId, onSuccess, onError)
                } else {
                    val codigoNumero = codigoLimpio.toLongOrNull()
                    if (codigoNumero == null) {
                        onError(Exception("No se encontro ningun grupo con ese codigo."))
                    } else {
                        db.collection("grupos")
                            .whereEqualTo("codigo", codigoNumero)
                            .get()
                            .addOnSuccessListener { resultadoNumero ->
                                if (!resultadoNumero.isEmpty) {
                                    agregarUsuarioAlGrupo(resultadoNumero, usuarioId, onSuccess, onError)
                                } else {
                                    onError(Exception("No se encontro ningun grupo con ese codigo."))
                                }
                            }
                            .addOnFailureListener { exception ->
                                onError(exception)
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    private fun agregarUsuarioAlGrupo(
        snapshot: QuerySnapshot,
        usuarioId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val documento = snapshot.documents.first()
        val grupo = documento.toObject(Grupo::class.java)
        val miembrosActuales = grupo?.miembrosIds ?: emptyList()

        if (miembrosActuales.contains(usuarioId)) {
            onError(Exception("Ya eres miembro de este grupo."))
            return
        }

        val nuevaLista = miembrosActuales.toMutableList()
        nuevaLista.add(usuarioId)

        documento.reference
            .update("miembrosIds", nuevaLista)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun unirseAGrupoPorCodigo(codigo: String, usuarioId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val codigoLimpio = codigo.trim().uppercase()

        db.collection("grupos")
            .whereEqualTo("codigo", codigoLimpio)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val documento = snapshot.documents.first()
                    val grupo = documento.toObject(Grupo::class.java)

                    if (grupo != null && !grupo.miembrosIds.contains(usuarioId)) {
                        val nuevaLista = grupo.miembrosIds.toMutableList()
                        nuevaLista.add(usuarioId)

                        documento.reference
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
