package com.exemplo.tarefasapp.telas

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.componentes.BottomBar
import com.exemplo.tarefasapp.dados.AppDatabase
import com.exemplo.tarefasapp.dados.Categoria
import com.exemplo.tarefasapp.dados.Tarefa
import com.exemplo.tarefasapp.navegacao.Rotas
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ListaTarefasScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    var tarefas by remember { mutableStateOf(listOf<Tarefa>()) }
    var categorias by remember { mutableStateOf(listOf<Categoria>()) }

    suspend fun carregar() {
        try {
            tarefas = db.tarefaDao().buscarTodos()
            categorias = db.categoriaDao().buscarTodos()
        } catch (e: Exception) {
            Log.e("ListaTarefas", "Erro ao buscar dados do banco", e)
        }
    }

    // Busca no banco assim que a tela abre
    LaunchedEffect(Unit) {
        carregar()
    }

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
                categorias = categorias,
                onAdicionar = { novaTarefa ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            db.tarefaDao().inserir(novaTarefa)
                            carregar()
                        } catch (e: Exception) {
                            Log.e("ListaTarefas", "Erro ao inserir tarefa", e)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(tarefas) { tarefa ->
                    CardTarefa(
                        tarefa = tarefa,
                        categoria = categorias.find { it.id == tarefa.categoriaId },
                        onClickCard = { navController.navigate(Rotas.detalheTarefa(tarefa.id)) },
                        onAlternarConcluida = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    db.tarefaDao().atualizar(tarefa.copy(concluida = !tarefa.concluida))
                                    carregar()
                                } catch (e: Exception) {
                                    Log.e("ListaTarefas", "Erro ao atualizar tarefa", e)
                                }
                            }
                        },
                        onRemover = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    db.tarefaDao().deletar(tarefa)
                                    carregar()
                                } catch (e: Exception) {
                                    Log.e("ListaTarefas", "Erro ao deletar tarefa", e)
                                }
                            }
                        }
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

@Preview(showBackground = true)
@Composable
fun ListaTarefasScreenPreview() {
    TarefasAppTheme {
        ListaTarefasScreen(navController = rememberNavController())
    }
}
