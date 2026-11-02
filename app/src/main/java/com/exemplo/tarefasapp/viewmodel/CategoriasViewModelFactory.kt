package com.exemplo.tarefasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository

class CategoriasViewModelFactory(
    private val categoriaRepository: CategoriaRepository,
    private val tarefaRepository: TarefaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriasViewModel(categoriaRepository, tarefaRepository) as T
    }
}
