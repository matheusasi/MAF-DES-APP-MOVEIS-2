package com.exemplo.tarefasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository

// O Android nao sabe criar um ViewModel com parametros, entao a Factory entrega os Repositories
class TarefasViewModelFactory(
    private val tarefaRepository: TarefaRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TarefasViewModel(tarefaRepository, categoriaRepository) as T
    }
}
