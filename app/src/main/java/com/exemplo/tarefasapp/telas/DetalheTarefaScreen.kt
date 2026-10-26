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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
fun DetalheTarefaScreen(navController: NavController, tarefaId: Int) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    var carregando by remember { mutableStateOf(true) }
    var tarefa by remember { mutableStateOf<Tarefa?>(null) }
    var categoria by remember { mutableStateOf<Categoria?>(null) }

    // Campos do formulario de edicao
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    // Busca so essa tarefa no banco, usando o id recebido pela navegacao
    suspend fun carregar() {
        try {
            val encontrada = db.tarefaDao().buscarPorId(tarefaId)
            tarefa = encontrada
            categoria = encontrada?.let { db.categoriaDao().buscarPorId(it.categoriaId) }
            if (encontrada != null) {
                titulo = encontrada.titulo
                descricao = encontrada.descricao
            }
        } catch (e: Exception) {
            Log.e("DetalheTarefa", "Erro ao buscar tarefa $tarefaId", e)
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
                title = { Text("Detalhe da Tarefa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        val tarefaAtual = tarefa

        if (tarefaAtual == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (!carregando) {
                    Text("Essa tarefa nao existe mais")
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = tarefaAtual.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = tarefaAtual.descricao)

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = tarefaAtual.concluida,
                    onCheckedChange = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                db.tarefaDao().atualizar(tarefaAtual.copy(concluida = !tarefaAtual.concluida))
                                carregar()
                            } catch (e: Exception) {
                                Log.e("DetalheTarefa", "Erro ao atualizar tarefa", e)
                            }
                        }
                    }
                )
                Text(text = if (tarefaAtual.concluida) "Concluida" else "Pendente")
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
                            color = Color(categoria!!.cor)
                        ) {}
                        Text(
                            text = "Categoria: ${categoria!!.nome}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val textoPrazo = when {
                tarefaAtual.concluida -> "Essa tarefa ja foi concluida"
                tarefaAtual.prazoDias <= 0 -> "O prazo dessa tarefa e hoje"
                tarefaAtual.prazoDias == 1 -> "Falta 1 dia para o prazo"
                else -> "Faltam ${tarefaAtual.prazoDias} dias para o prazo"
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
                label = { Text("Titulo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descricao") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (titulo.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            db.tarefaDao().atualizar(
                                tarefaAtual.copy(titulo = titulo, descricao = descricao)
                            )
                            carregar()
                        } catch (e: Exception) {
                            Log.e("DetalheTarefa", "Erro ao salvar edicao", e)
                        }
                    }
                }
            }) {
                Text("Salvar alteracoes")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalheTarefaScreenPreview() {
    TarefasAppTheme {
        DetalheTarefaScreen(navController = rememberNavController(), tarefaId = 1)
    }
}
