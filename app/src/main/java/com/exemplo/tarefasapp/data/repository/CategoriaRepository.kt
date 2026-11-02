package com.exemplo.tarefasapp.data.repository

import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.CategoriaDao
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val categoriaDao: CategoriaDao) {

    fun buscarTodos(): Flow<List<Categoria>> {
        return categoriaDao.buscarTodos()
    }

    suspend fun inserir(categoria: Categoria) {
        categoriaDao.inserir(categoria)
    }

    suspend fun atualizar(categoria: Categoria) {
        categoriaDao.atualizar(categoria)
    }

    suspend fun deletar(categoria: Categoria) {
        categoriaDao.deletar(categoria)
    }
}
