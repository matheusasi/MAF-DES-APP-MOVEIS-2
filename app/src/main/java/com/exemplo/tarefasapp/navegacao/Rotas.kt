package com.exemplo.tarefasapp.navegacao

object Rotas {
    const val HOME = "home"
    const val LISTA_TAREFAS = "lista_tarefas"
    const val DETALHE_TAREFA = "detalhe_tarefa/{tarefaId}"
    const val LISTA_CATEGORIAS = "lista_categorias"
    const val DETALHE_CATEGORIA = "detalhe_categoria/{categoriaId}"
    const val PERFIL = "perfil"
    const val SOBRE = "sobre"

    fun detalheTarefa(tarefaId: Int): String {
        return "detalhe_tarefa/$tarefaId"
    }

    fun detalheCategoria(categoriaId: Int): String {
        return "detalhe_categoria/$categoriaId"
    }
}
