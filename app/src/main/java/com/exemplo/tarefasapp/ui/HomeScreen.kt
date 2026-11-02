package com.exemplo.tarefasapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.ui.componentes.BottomBar
import com.exemplo.tarefasapp.ui.navegacao.Rotas
import com.exemplo.tarefasapp.viewmodel.TarefasViewModel
import com.exemplo.tarefasapp.viewmodel.TarefasViewModelFactory

@Composable
fun HomeScreen(
    navController: NavController,
    tarefaRepository: TarefaRepository,
    categoriaRepository: CategoriaRepository
) {
    val viewModel: TarefasViewModel = viewModel(
        factory = TarefasViewModelFactory(tarefaRepository, categoriaRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = { BottomBar(navController, Rotas.HOME) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Olá!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Isso é um resumo do seu organizador de tarefas",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rotas.LISTA_TAREFAS) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Tarefas pendentes", style = MaterialTheme.typography.titleMedium)
                    Text(text = "${uiState.tarefasPendentes} tarefa(s) ainda por fazer")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rotas.LISTA_CATEGORIAS) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Categorias", style = MaterialTheme.typography.titleMedium)
                    Text(text = "${uiState.categorias.size} categoria(s) cadastrada(s)")
                }
            }
        }
    }
}
