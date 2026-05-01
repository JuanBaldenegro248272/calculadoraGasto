package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.UsuarioDao
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {
    suspend fun insertarUsuario(usuario: Usuario): Long {
        return usuarioDao.insertUsuario(usuario)
    }

    suspend fun updateUsuario(usuario: Usuario) {
        usuarioDao.updateUsuario(usuario)
    }

    suspend fun loginUsuario(correo: String, contrasena: String): Usuario? {
        return usuarioDao.loginUsuario(correo, contrasena)
    }

    suspend fun obtenerUusarioPorId(usuarioId: Int): Usuario? {
        return usuarioDao.getUsuarioById(usuarioId)
    }

    suspend fun verificarCorreoExistente(correo: String): Usuario? {
        return usuarioDao.verificarCorreoExistente(correo)
    }
}