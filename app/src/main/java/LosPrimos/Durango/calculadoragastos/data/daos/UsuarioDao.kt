package LosPrimos.Durango.calculadoragastos.data.daos

import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario): Long

    @Update
    suspend fun updateUsuario(usuario: Usuario): Int

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND hashContrasena = :contrasena LIMIT 1")
    suspend fun loginUsuario(correo: String, contrasena: String): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE idUsuario = :id LIMIT 1")
    suspend fun getUsuarioById(id: Int): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun verificarCorreoExistente(correo: String): List<Usuario>
}
