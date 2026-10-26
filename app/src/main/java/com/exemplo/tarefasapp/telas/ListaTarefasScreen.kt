package com.exemplo.tarefasapp.telas

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.componentes.BottomBar
import com.exemplo.tarefasapp.dados.AppDados
import com.exemplo.tarefasapp.dados.Tarefa
import com.exemplo.tarefasapp.navegacao.Rotas
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@Composable
fun ListaTarefasScreen(navController: NavController) {
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

            FormularioNovaTarefa()

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(AppDados.tarefas) { tarefa ->
                    CardTarefa(
                        tarefa = tarefa,
                        onClickCard = { navController.navigate(Rotas.detalheTarefa(tarefa.id)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun FormularioNovaTarefa() {
    var titulo by remember { mutableStateOf("") }
    var categoriaSelecionadaId by remember { mutableStateOf(AppDados.categorias.first().id) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AppDados.categorias.forEach { categoria ->
            Button(
                onClick = { categoriaSelecionadaId = categoria.id },
                colors = if (categoriaSelecionadaId == categoria.id) {
                    ButtonDefaults.buttonColors(containerColor = categoria.cor)
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
            if (titulo.isNotBlank()) {
                AppDados.tarefas.add(
                    Tarefa(
                        id = AppDados.proximoIdTarefa(),
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
private fun CardTarefa(tarefa: Tarefa, onClickCard: () -> Unit) {
    val categoria = AppDados.buscarCategoria(tarefa.categoriaId)

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
                onCheckedChange = { AppDados.alternarConcluida(tarefa.id) }
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
                        color = categoria.cor
                    )
                }
            }

            IconButton(onClick = { AppDados.tarefas.remove(tarefa) }) {
                Icon(Icons.Default.Delete, contentDescription = "Remover tarefa")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTarefasScreenPreview() {
    TarefasAppTheme {
        ListaTarefasScreen(navController = rememberNavController())
    }
}
