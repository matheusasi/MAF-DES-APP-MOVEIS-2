package com.exemplo.tarefasapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.exemplo.tarefasapp.viewmodel.CategoriasViewModel
import com.exemplo.tarefasapp.viewmodel.CategoriasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheCategoriaScreen(
    navController: NavController,
    categoriaId: Int,
    categoriaRepository: CategoriaRepository,
    tarefaRepository: TarefaRepository
) {
    val viewModel: CategoriasViewModel = viewModel(
        factory = CategoriasViewModelFactory(categoriaRepository, tarefaRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val categoria = uiState.categorias.find { it.id == categoriaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe da Categoria") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (categoria == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!uiState.carregando) {
                    Text("Essa categoria não existe mais")
                }
            }
        } else {
            ConteudoDetalheCategoria(
                categoria = categoria,
                tarefasDaCategoria = uiState.tarefas.filter { it.categoriaId == categoria.id },
                onSalvarNome = { nome -> viewModel.renomearCategoria(categoria, nome) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ConteudoDetalheCategoria(
    categoria: Categoria,
    tarefasDaCategoria: List<Tarefa>,
    onSalvarNome: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nome by remember(categoria.id) { mutableStateOf(categoria.nome) }
    val quantidadeConcluidas = tarefasDaCategoria.count { it.concluida }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = Color(categoria.cor)
            ) {}
            Text(
                text = categoria.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "$quantidadeConcluidas de ${tarefasDaCategoria.size} tarefas concluídas")

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da categoria") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (nome.isNotBlank()) {
                    onSalvarNome(nome)
                }
            }) {
                Text("Salvar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tarefasDaCategoria.isEmpty()) {
            Text("Nenhuma tarefa nessa categoria ainda")
        } else {
            tarefasDaCategoria.forEach { tarefa ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = tarefa.titulo,
                            textDecoration = if (tarefa.concluida) TextDecoration.LineThrough else null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
