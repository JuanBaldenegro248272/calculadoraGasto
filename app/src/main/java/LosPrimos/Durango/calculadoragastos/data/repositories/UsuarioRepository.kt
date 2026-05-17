package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.UsuarioDao
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {
    suspend fun insertarUsuario(usuario: Usuario): Long {
        val resultado = usuarioDao.insertUsuario(usuario)

        if (resultado == -1L) {
            usuarioDao.updateUsuario(usuario)
        }

        return resultado
    }

    suspend fun updateUsuario(usuario: Usuario) {
        usuarioDao.updateUsuario(usuario)
    }

    suspend fun loginUsuario(correo: String, contrasena: String): Usuario? {
        return usuarioDao.loginUsuario(correo, contrasena)
    }

    fun obtenerUsuarioPorId(usuarioId: String): Flow<Usuario?> {
        return usuarioDao.getUsuarioById(usuarioId)
    }

    suspend fun existeUsuario(id: String): Boolean {
        return usuarioDao.getUsuarioByIdSuspend(id) != null
    }

    suspend fun verificarCorreoExistente(correo: String): Usuario? {
        return usuarioDao.verificarCorreoExistente(correo)
    }
}
