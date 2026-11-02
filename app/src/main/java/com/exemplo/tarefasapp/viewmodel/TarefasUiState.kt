package com.exemplo.tarefasapp.viewmodel

import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.Tarefa

// Tudo que as telas de tarefa (inicio, lista e detalhe) precisam para se desenhar
data class TarefasUiState(
    val tarefas: List<Tarefa> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val carregando: Boolean = true
) {
    val tarefasPendentes: Int
        get() = tarefas.count { !it.concluida }
}
