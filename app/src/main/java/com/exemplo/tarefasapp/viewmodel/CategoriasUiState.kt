package com.exemplo.tarefasapp.viewmodel

import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.Tarefa

// Tudo que as telas de categoria (lista e detalhe) precisam para se desenhar
data class CategoriasUiState(
    val categorias: List<Categoria> = emptyList(),
    val tarefas: List<Tarefa> = emptyList(),
    val carregando: Boolean = true
)
