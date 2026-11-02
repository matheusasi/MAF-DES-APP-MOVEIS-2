package com.exemplo.tarefasapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    // Flow nao precisa de suspend e o Room emite a lista de novo sempre que a tabela muda
    @Query("SELECT * FROM tarefas")
    fun buscarTodos(): Flow<List<Tarefa>>

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)
}
