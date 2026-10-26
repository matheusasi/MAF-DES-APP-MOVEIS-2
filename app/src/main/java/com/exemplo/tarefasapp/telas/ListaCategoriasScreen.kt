package com.exemplo.tarefasapp.telas

import android.util.Log
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

// As cores sao guardadas no banco como Long (ARGB), ja que o Room nao salva Color
private val coresDisponiveis = listOf(
    0xFF6750A4,
    0xFF4CAF50,
    0xFFEF6C00,
    0xFFD32F2F,
    0xFF0288D1
)

@Composable
fun ListaCategoriasScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    var categorias by remember { mutableStateOf(listOf<Categoria>()) }
    var tarefas by remember { mutableStateOf(listOf<Tarefa>()) }

    suspend fun carregar() {
        try {
            categorias = db.categoriaDao().buscarTodos()
            tarefas = db.tarefaDao().buscarTodos()
        } catch (e: Exception) {
            Log.e("ListaCategorias", "Erro ao buscar dados do banco", e)
        }
    }

    // Busca no banco assim que a tela abre
    LaunchedEffect(Unit) {
        carregar()
    }

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
                onAdicionar = { novaCategoria ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            db.categoriaDao().inserir(novaCategoria)
                            carregar()
                        } catch (e: Exception) {
                            Log.e("ListaCategorias", "Erro ao inserir categoria", e)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(categorias) { categoria ->
                    CardCategoria(
                        categoria = categoria,
                        quantidadeTarefas = tarefas.count { it.categoriaId == categoria.id },
                        onClickCard = { navController.navigate(Rotas.detalheCategoria(categoria.id)) },
                        onRemover = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    db.categoriaDao().deletar(categoria)
                                    carregar()
                                } catch (e: Exception) {
                                    Log.e("ListaCategorias", "Erro ao deletar categoria", e)
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

@Preview(showBackground = true)
@Composable
fun ListaCategoriasScreenPreview() {
    TarefasAppTheme {
        ListaCategoriasScreen(navController = rememberNavController())
    }
}
