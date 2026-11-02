package com.exemplo.tarefasapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val categoriaRepository: CategoriaRepository,
    private val tarefaRepository: TarefaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriasUiState())
    val uiState: StateFlow<CategoriasUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                categoriaRepository.buscarTodos(),
                tarefaRepository.buscarTodos()
            ) { categorias, tarefas ->
                CategoriasUiState(categorias = categorias, tarefas = tarefas, carregando = false)
            }.catch { erro ->
                Log.e("CategoriasViewModel", "Erro ao buscar dados do banco", erro)
            }.collect { novoEstado ->
                _uiState.value = novoEstado
            }
        }
    }

    fun adicionarCategoria(categoria: Categoria) {
        viewModelScope.launch {
            try {
                categoriaRepository.inserir(categoria)
            } catch (e: Exception) {
                Log.e("CategoriasViewModel", "Erro ao inserir categoria", e)
            }
        }
    }

    fun renomearCategoria(categoria: Categoria, nome: String) {
        viewModelScope.launch {
            try {
                categoriaRepository.atualizar(categoria.copy(nome = nome))
            } catch (e: Exception) {
                Log.e("CategoriasViewModel", "Erro ao salvar categoria", e)
            }
        }
    }

    fun removerCategoria(categoria: Categoria) {
        viewModelScope.launch {
            try {
                categoriaRepository.deletar(categoria)
            } catch (e: Exception) {
                Log.e("CategoriasViewModel", "Erro ao deletar categoria", e)
            }
        }
    }
}
