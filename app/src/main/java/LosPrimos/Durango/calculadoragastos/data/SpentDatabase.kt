package LosPrimos.Durango.calculadoragastos.data

import LosPrimos.Durango.calculadoragastos.data.daos.GastoDao
import LosPrimos.Durango.calculadoragastos.data.daos.IngresoDao
import LosPrimos.Durango.calculadoragastos.data.daos.UsuarioDao
import LosPrimos.Durango.calculadoragastos.data.entities.Categoria
import LosPrimos.Durango.calculadoragastos.data.entities.Gasto
import LosPrimos.Durango.calculadoragastos.data.entities.Ingreso
import LosPrimos.Durango.calculadoragastos.data.entities.Tarjeta
import LosPrimos.Durango.calculadoragastos.data.entities.Usuario
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    Usuario::class,
    Gasto::class,
    Ingreso::class,
    Categoria::class,
    Tarjeta::class], version = 3, exportSchema = false)
abstract class SpentDatabase : RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao
    abstract fun gastoDao(): GastoDao
    abstract fun ingresoDao(): IngresoDao

    companion object {
        @Volatile
        private var INSTANCE: LosPrimos.Durango.calculadoragastos.data.SpentDatabase? = null

        fun getDatabase(context: Context): LosPrimos.Durango.calculadoragastos.data.SpentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LosPrimos.Durango.calculadoragastos.data.SpentDatabase::class.java,
                    "spent_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}