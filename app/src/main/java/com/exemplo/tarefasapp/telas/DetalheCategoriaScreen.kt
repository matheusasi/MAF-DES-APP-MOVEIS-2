package com.exemplo.tarefasapp.telas

import android.util.Log
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
import com.exemplo.tarefasapp.dados.AppDatabase
import com.exemplo.tarefasapp.dados.Categoria
import com.exemplo.tarefasapp.dados.Tarefa
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheCategoriaScreen(navController: NavController, categoriaId: Int) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    var carregando by remember { mutableStateOf(true) }
    var categoria by remember { mutableStateOf<Categoria?>(null) }
    var tarefasDaCategoria by remember { mutableStateOf(listOf<Tarefa>()) }
    var nome by remember { mutableStateOf("") }

    // Busca a categoria pelo id e so as tarefas dela
    suspend fun carregar() {
        try {
            val encontrada = db.categoriaDao().buscarPorId(categoriaId)
            categoria = encontrada
            if (encontrada != null) {
                nome = encontrada.nome
                tarefasDaCategoria = db.tarefaDao().buscarPorCategoria(encontrada.id)
            }
        } catch (e: Exception) {
            Log.e("DetalheCategoria", "Erro ao buscar categoria $categoriaId", e)
        } finally {
            carregando = false
        }
    }

    LaunchedEffect(Unit) {
        carregar()
    }

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
        val categoriaAtual = categoria

        if (categoriaAtual == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!carregando) {
                    Text("Essa categoria não existe mais")
                }
            }
            return@Scaffold
        }

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
                    color = Color(categoriaAtual.cor)
                ) {}
                Text(
                    text = categoriaAtual.nome,
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
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                db.categoriaDao().atualizar(categoriaAtual.copy(nome = nome))
                                carregar()
                            } catch (e: Exception) {
                                Log.e("DetalheCategoria", "Erro ao salvar edicao", e)
                            }
                        }
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
}

@Preview(showBackground = true)
@Composable
fun DetalheCategoriaScreenPreview() {
    TarefasAppTheme {
        DetalheCategoriaScreen(navController = rememberNavController(), categoriaId = 1)
    }
}
