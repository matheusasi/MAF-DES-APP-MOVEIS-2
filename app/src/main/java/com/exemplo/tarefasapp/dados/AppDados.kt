package com.exemplo.tarefasapp.dados

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

object AppDados {

    val categorias = mutableStateListOf(
        Categoria(id = 1, nome = "Faculdade", cor = Color(0xFF6750A4)),
        Categoria(id = 2, nome = "Casa", cor = Color(0xFF4CAF50)),
        Categoria(id = 3, nome = "Pessoal", cor = Color(0xFFEF6C00))
    )

    val tarefas = mutableStateListOf(
        Tarefa(id = 1, titulo = "Entregar trabalho de DAM", descricao = "Terminar o app de tarefas e categorias", categoriaId = 1, concluida = false, prazoDias = 3),
        Tarefa(id = 2, titulo = "Estudar para a prova", descricao = "Revisar o conteudo de Navigation Compose", categoriaId = 1, concluida = false, prazoDias = 7),
        Tarefa(id = 3, titulo = "Lavar roupa", descricao = "Colocar a maquina para lavar antes do meio dia", categoriaId = 2, concluida = true, prazoDias = 0),
        Tarefa(id = 4, titulo = "Ir na academia", descricao = "Treino de pernas", categoriaId = 3, concluida = false, prazoDias = 1)
    )

    fun proximoIdTarefa(): Int {
        val maiorId = tarefas.maxOfOrNull { it.id } ?: 0
        return maiorId + 1
    }

    fun proximoIdCategoria(): Int {
        val maiorId = categorias.maxOfOrNull { it.id } ?: 0
        return maiorId + 1
    }

    fun buscarCategoria(id: Int): Categoria? {
        return categorias.find { it.id == id }
    }

    fun buscarTarefa(id: Int): Tarefa? {
        return tarefas.find { it.id == id }
    }

    fun tarefasDaCategoria(categoriaId: Int): List<Tarefa> {
        return tarefas.filter { it.categoriaId == categoriaId }
    }

    fun alternarConcluida(tarefaId: Int) {
        val indice = tarefas.indexOfFirst { it.id == tarefaId }
        if (indice != -1) {
            val tarefa = tarefas[indice]
            tarefas[indice] = tarefa.copy(concluida = !tarefa.concluida)
        }
    }
}
