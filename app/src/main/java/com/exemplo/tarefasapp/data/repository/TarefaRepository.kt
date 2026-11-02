package com.exemplo.tarefasapp.data.repository

import com.exemplo.tarefasapp.data.Tarefa
import com.exemplo.tarefasapp.data.TarefaDao
import kotlinx.coroutines.flow.Flow

// So repassa para o DAO, quem usa o Repository nao sabe de onde os dados vem
class TarefaRepository(private val tarefaDao: TarefaDao) {

    fun buscarTodos(): Flow<List<Tarefa>> {
        return tarefaDao.buscarTodos()
    }

    suspend fun inserir(tarefa: Tarefa) {
        tarefaDao.inserir(tarefa)
    }

    suspend fun atualizar(tarefa: Tarefa) {
        tarefaDao.atualizar(tarefa)
    }

    suspend fun deletar(tarefa: Tarefa) {
        tarefaDao.deletar(tarefa)
    }
}
