package com.exemplo.tarefasapp.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.dados.AppDados
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheCategoriaScreen(navController: NavController, categoriaId: Int) {
    val categoria = AppDados.buscarCategoria(categoriaId)

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
                Text("Essa categoria nao existe mais")
            }
            return@Scaffold
        }

        val tarefasDaCategoria = AppDados.tarefasDaCategoria(categoria.id)
        val quantidadeConcluidas = tarefasDaCategoria.count { it.concluida }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(28.dp),
                    shape = CircleShape,
                    color = categoria.cor
                ) {}
                Text(
                    text = categoria.nome,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "$quantidadeConcluidas de ${tarefasDaCategoria.size} tarefas concluidas")

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
}

@Preview(showBackground = true)
@Composable
fun DetalheCategoriaScreenPreview() {
    TarefasAppTheme {
        DetalheCategoriaScreen(navController = rememberNavController(), categoriaId = 1)
    }
}
