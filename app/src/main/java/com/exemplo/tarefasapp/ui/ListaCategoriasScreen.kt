package com.exemplo.tarefasapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.ui.componentes.BottomBar
import com.exemplo.tarefasapp.ui.navegacao.Rotas
import com.exemplo.tarefasapp.viewmodel.CategoriasViewModel
import com.exemplo.tarefasapp.viewmodel.CategoriasViewModelFactory

// As cores sao guardadas no banco como Long (ARGB), ja que o Room nao salva Color
private val coresDisponiveis = listOf(
    0xFF6750A4,
    0xFF4CAF50,
    0xFFEF6C00,
    0xFFD32F2F,
    0xFF0288D1
)

@Composable
fun ListaCategoriasScreen(
    navController: NavController,
    categoriaRepository: CategoriaRepository,
    tarefaRepository: TarefaRepository
) {
    val viewModel: CategoriasViewModel = viewModel(
        factory = CategoriasViewModelFactory(categoriaRepository, tarefaRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { BottomBar(navController, Rotas.LISTA_CATEGORIAS) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Minhas Categorias",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormularioNovaCategoria(
                onAdicionar = { novaCategoria -> viewModel.adicionarCategoria(novaCategoria) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(uiState.categorias) { categoria ->
                    CardCategoria(
                        categoria = categoria,
                        quantidadeTarefas = uiState.tarefas.count { it.categoriaId == categoria.id },
                        onClickCard = { navController.navigate(Rotas.detalheCategoria(categoria.id)) },
                        onRemover = { viewModel.removerCategoria(categoria) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun FormularioNovaCategoria(onAdicionar: (Categoria) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var corSelecionada by remember { mutableStateOf(coresDisponiveis.first()) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        coresDisponiveis.forEach { cor ->
            Surface(
                modifier = Modifier
                    .size(32.dp),
                shape = CircleShape,
                color = Color(cor),
                onClick = { corSelecionada = cor },
                border = if (corSelecionada == cor) {
                    BorderStroke(2.dp, Color.Black)
                } else {
                    null
                }
            ) {}
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nova categoria") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (nome.isNotBlank()) {
                onAdicionar(Categoria(nome = nome, cor = corSelecionada))
                nome = ""
            }
        }) {
            Text("Adicionar")
        }
    }
}

@Composable
private fun CardCategoria(
    categoria: Categoria,
    quantidadeTarefas: Int,
    onClickCard: () -> Unit,
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
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = Color(categoria.cor)
            ) {}

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = categoria.nome)
                Text(
                    text = "$quantidadeTarefas tarefa(s)",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover categoria")
            }
        }
    }
}

