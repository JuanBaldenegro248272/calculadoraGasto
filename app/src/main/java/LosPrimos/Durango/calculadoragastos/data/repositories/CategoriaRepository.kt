package LosPrimos.Durango.calculadoragastos.data.repositories

import LosPrimos.Durango.calculadoragastos.data.daos.CategoriaDao
import LosPrimos.Durango.calculadoragastos.data.entities.Categoria
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    suspend fun insertarCategoria(categoria: Categoria): Long{
        return categoriaDao.insertCategoria(categoria)
    }

    suspend fun deleteCategoria(categoria: Categoria): Int {
        return categoriaDao.deleteCategoria(categoria)
    }

    suspend fun updateCategoria(categoria: Categoria): Int{
        return categoriaDao.updateCategoria(categoria)
    }

    suspend fun getCategoria(idCategoria: Int): Categoria?{
        return categoriaDao.getCategoriaById(idCategoria).firstOrNull()
    }

     fun getAllCategorias(): Flow<List<Categoria>> {
        return categoriaDao.getAllCategorias()
    }
}