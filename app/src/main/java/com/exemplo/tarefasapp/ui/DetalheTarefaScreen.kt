package com.exemplo.tarefasapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.exemplo.tarefasapp.data.Categoria
import com.exemplo.tarefasapp.data.Tarefa
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.viewmodel.TarefasViewModel
import com.exemplo.tarefasapp.viewmodel.TarefasViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheTarefaScreen(
    navController: NavController,
    tarefaId: Int,
    tarefaRepository: TarefaRepository,
    categoriaRepository: CategoriaRepository
) {
    val viewModel: TarefasViewModel = viewModel(
        factory = TarefasViewModelFactory(tarefaRepository, categoriaRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tarefa = uiState.tarefas.find { it.id == tarefaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe da Tarefa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (tarefa == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!uiState.carregando) {
                    Text("Essa tarefa não existe mais")
                }
            }
        } else {
            ConteudoDetalheTarefa(
                tarefa = tarefa,
                categoria = uiState.categorias.find { it.id == tarefa.categoriaId },
                todasCategorias = uiState.categorias,
                onAlternarConcluida = { viewModel.alternarConcluida(tarefa) },
                onTrocarCategoria = { categoriaId -> viewModel.trocarCategoria(tarefa, categoriaId) },
                onSalvar = { titulo, descricao -> viewModel.editarTarefa(tarefa, titulo, descricao) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ConteudoDetalheTarefa(
    tarefa: Tarefa,
    categoria: Categoria?,
    todasCategorias: List<Categoria>,
    onAlternarConcluida: () -> Unit,
    onTrocarCategoria: (Int) -> Unit,
    onSalvar: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuAberto by remember { mutableStateOf(false) }

    // Campos do formulario de edicao, comecam com os valores da tarefa
    var titulo by remember(tarefa.id) { mutableStateOf(tarefa.titulo) }
    var descricao by remember(tarefa.id) { mutableStateOf(tarefa.descricao) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = tarefa.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = tarefa.descricao)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = tarefa.concluida,
                onCheckedChange = { onAlternarConcluida() }
            )
            Text(text = if (tarefa.concluida) "Concluída" else "Pendente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (categoria != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = CircleShape,
                        color = Color(categoria.cor)
                    ) {}
                    Text(
                        text = "Categoria: ${categoria.nome}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { menuAberto = true }) {
            Text("Alterar categoria")
        }
        DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
            todasCategorias.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao.nome) },
                    onClick = {
                        menuAberto = false
                        onTrocarCategoria(opcao.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val textoPrazo = when {
            tarefa.concluida -> "Essa tarefa já foi concluída"
            tarefa.prazoDias <= 0 -> "O prazo dessa tarefa é hoje"
            tarefa.prazoDias == 1 -> "Falta 1 dia para o prazo"
            else -> "Faltam ${tarefa.prazoDias} dias para o prazo"
        }
        Text(text = textoPrazo, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Editar tarefa",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (titulo.isNotBlank()) {
                onSalvar(titulo, descricao)
            }
        }) {
            Text("Salvar alterações")
        }
    }
}
