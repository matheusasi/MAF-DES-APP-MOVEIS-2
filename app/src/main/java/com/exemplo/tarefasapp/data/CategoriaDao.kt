package com.exemplo.tarefasapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert
    suspend fun inserir(categoria: Categoria)

    @Query("SELECT * FROM categorias")
    fun buscarTodos(): Flow<List<Categoria>>

    @Update
    suspend fun atualizar(categoria: Categoria)

    @Delete
    suspend fun deletar(categoria: Categoria)
}
