package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.UsuarioDao
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao){

    suspend fun insertarUsuario(usuario: Usuario): Int{
        return usuarioDao.insertUsuario(usuario)
    }

    suspend fun updateUsuario(usuario: Usuario){
        return usuarioDao.updateUsuario(usuario)
    }
}