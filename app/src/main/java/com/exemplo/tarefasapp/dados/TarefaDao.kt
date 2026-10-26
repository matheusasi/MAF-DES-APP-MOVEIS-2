package com.exemplo.tarefasapp.dados

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TarefaDao {

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas")
    suspend fun buscarTodos(): List<Tarefa>

    @Query("SELECT * FROM tarefas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Tarefa?

    @Query("SELECT * FROM tarefas WHERE categoriaId = :categoriaId")
    suspend fun buscarPorCategoria(categoriaId: Int): List<Tarefa>

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)
}
