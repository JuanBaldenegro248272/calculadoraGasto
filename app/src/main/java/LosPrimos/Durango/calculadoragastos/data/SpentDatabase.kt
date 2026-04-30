package LosPrimos.Durango.calculadoragastos.data

import LosPrimos.Durango.calculadoragastos.data.daos.UsuarioDao
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class SpentDatabase : RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: LosPrimos.Durango.calculadoragastos.data.SpentDatabase? = null

        fun getDatabase(context: Context): LosPrimos.Durango.calculadoragastos.data.SpentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LosPrimos.Durango.calculadoragastos.data.SpentDatabase::class.java,
                    "spent_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}