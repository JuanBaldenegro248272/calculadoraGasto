package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.entities.Grupo
import com.google.firebase.firestore.FirebaseFirestore


class GrupoRepository(){

    private val db = FirebaseFirestore.getInstance()

    fun crearGrupo(grupo: Grupo){
        db.collection("grupos").add(grupo)
    }

    fun obtenerGrupos(usuarioId: Int?, resultadoGrupos: (List<Grupo>) -> Unit){
        if (usuarioId != null) {
            db.collection("grupos").whereArrayContains("miembros", usuarioId)
                .get().addOnSuccessListener { resultado -> val grupos = resultado.toObjects(Grupo::class.java)
                    resultadoGrupos(grupos)
                }
        }
    }

}
