package LosPrimos.Durango.calculadoragastos.viewModel

import LosPrimos.Durango.calculadoragastos.data.entities.Categoria
import LosPrimos.Durango.calculadoragastos.data.repositories.CategoriaRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: CategoriaRepository): ViewModel(){

    suspend fun obtenerCategoria(idCategoria: Int): Categoria?{
        return repository.getCategoria(idCategoria)
    }

    fun obtenerCategorias(): Flow<List<Categoria>> {
        return repository.getAllCategorias()
    }

    fun insertarCategorias(){
        viewModelScope.launch { 
            repository.insertarCategoria(Categoria(
                idCategoria = 1,
                nombre = "Vivienda",
                color = null,
                icono = null
            ))
            repository.insertarCategoria(Categoria(
                idCategoria = 2,
                nombre = "Entretenimiento",
                color = null,
                icono = null
            ))
            repository.insertarCategoria(Categoria(
                idCategoria = 3,
                nombre = "Transporte",
                color = null,
                icono = null
            ))
            repository.insertarCategoria(Categoria(
                idCategoria = 4,
                nombre = "Alimentacion",
                color = null,
                icono = null
            ))
            repository.insertarCategoria(Categoria(
                idCategoria = 5,
                nombre = "Salud",
                color = null,
                icono = null
            ))
            repository.insertarCategoria(Categoria(
                idCategoria = 6,
                nombre = "Otros",
                color = null,
                icono = null
            ))
        }
    }
    
}