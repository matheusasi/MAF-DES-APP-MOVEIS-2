package com.exemplo.tarefasapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.Tarefa
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.ui.componentes.BottomBar
import com.exemplo.tarefasapp.ui.navegacao.Rotas
import com.exemplo.tarefasapp.viewmodel.TarefasViewModel
import com.exemplo.tarefasapp.viewmodel.TarefasViewModelFactory

@Composable
fun ListaTarefasScreen(
    navController: NavController,
    tarefaRepository: TarefaRepository,
    categoriaRepository: CategoriaRepository
) {
    val viewModel: TarefasViewModel = viewModel(
        factory = TarefasViewModelFactory(tarefaRepository, categoriaRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { BottomBar(navController, Rotas.LISTA_TAREFAS) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Minhas Tarefas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormularioNovaTarefa(
                categorias = uiState.categorias,
                onAdicionar = { novaTarefa -> viewModel.adicionarTarefa(novaTarefa) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(uiState.tarefas) { tarefa ->
                    CardTarefa(
                        tarefa = tarefa,
                        categoria = uiState.categorias.find { it.id == tarefa.categoriaId },
                        onClickCard = { navController.navigate(Rotas.detalheTarefa(tarefa.id)) },
                        onAlternarConcluida = { viewModel.alternarConcluida(tarefa) },
                        onRemover = { viewModel.removerTarefa(tarefa) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun FormularioNovaTarefa(categorias: List<Categoria>, onAdicionar: (Tarefa) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var categoriaEscolhidaId by remember { mutableStateOf<Int?>(null) }

    // Enquanto nada foi escolhido, usa a primeira categoria do banco
    val categoriaSelecionadaId = categoriaEscolhidaId ?: categorias.firstOrNull()?.id

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categorias.forEach { categoria ->
            Button(
                onClick = { categoriaEscolhidaId = categoria.id },
                colors = if (categoriaSelecionadaId == categoria.id) {
                    ButtonDefaults.buttonColors(containerColor = Color(categoria.cor))
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(categoria.nome)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Nova tarefa") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (titulo.isNotBlank() && categoriaSelecionadaId != null) {
                onAdicionar(
                    Tarefa(
                        titulo = titulo,
                        descricao = "Tarefa criada pelo app",
                        categoriaId = categoriaSelecionadaId,
                        concluida = false,
                        prazoDias = 1
                    )
                )
                titulo = ""
            }
        }) {
            Text("Adicionar")
        }
    }
}

@Composable
private fun CardTarefa(
    tarefa: Tarefa,
    categoria: Categoria?,
    onClickCard: () -> Unit,
    onAlternarConcluida: () -> Unit,
    onRemover: () -> Unit
) {
    Card(
        onClick = onClickCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarefa.concluida,
                onCheckedChange = { onAlternarConcluida() }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = tarefa.titulo,
                    textDecoration = if (tarefa.concluida) TextDecoration.LineThrough else null
                )
                if (categoria != null) {
                    Text(
                        text = categoria.nome,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(categoria.cor)
                    )
                }
            }

            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover tarefa")
            }
        }
    }
}

