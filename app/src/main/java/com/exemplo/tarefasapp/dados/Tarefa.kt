package com.exemplo.tarefasapp.dados

data class Tarefa(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val categoriaId: Int,
    val concluida: Boolean,
    val prazoDias: Int
)
