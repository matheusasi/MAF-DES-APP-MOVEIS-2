package com.exemplo.tarefasapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplo.tarefasapp.data.Tarefa
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TarefasViewModel(
    private val tarefaRepository: TarefaRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TarefasUiState())
    val uiState: StateFlow<TarefasUiState> = _uiState.asStateFlow()

    // Escuta as duas tabelas, o Room avisa sozinho quando algo muda
    init {
        viewModelScope.launch {
            combine(
                tarefaRepository.buscarTodos(),
                categoriaRepository.buscarTodos()
            ) { tarefas, categorias ->
                TarefasUiState(tarefas = tarefas, categorias = categorias, carregando = false)
            }.catch { erro ->
                Log.e("TarefasViewModel", "Erro ao buscar dados do banco", erro)
            }.collect { novoEstado ->
                _uiState.value = novoEstado
            }
        }
    }

    fun adicionarTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            try {
                tarefaRepository.inserir(tarefa)
            } catch (e: Exception) {
                Log.e("TarefasViewModel", "Erro ao inserir tarefa", e)
            }
        }
    }

    fun alternarConcluida(tarefa: Tarefa) {
        atualizarTarefa(tarefa.copy(concluida = !tarefa.concluida))
    }

    fun trocarCategoria(tarefa: Tarefa, categoriaId: Int) {
        atualizarTarefa(tarefa.copy(categoriaId = categoriaId))
    }

    fun editarTarefa(tarefa: Tarefa, titulo: String, descricao: String) {
        atualizarTarefa(tarefa.copy(titulo = titulo, descricao = descricao))
    }

    fun removerTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            try {
                tarefaRepository.deletar(tarefa)
            } catch (e: Exception) {
                Log.e("TarefasViewModel", "Erro ao deletar tarefa", e)
            }
        }
    }

    private fun atualizarTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            try {
                tarefaRepository.atualizar(tarefa)
            } catch (e: Exception) {
                Log.e("TarefasViewModel", "Erro ao atualizar tarefa", e)
            }
        }
    }
}
