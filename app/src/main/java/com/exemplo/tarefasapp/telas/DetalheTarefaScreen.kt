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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.dados.AppDados
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheTarefaScreen(navController: NavController, tarefaId: Int) {
    val tarefa = AppDados.buscarTarefa(tarefaId)

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
                Text("Essa tarefa nao existe mais")
            }
            return@Scaffold
        }

        val categoria = AppDados.buscarCategoria(tarefa.categoriaId)

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
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
                    onCheckedChange = { AppDados.alternarConcluida(tarefa.id) }
                )
                Text(text = if (tarefa.concluida) "Concluida" else "Pendente")
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
                            color = categoria.cor
                        ) {}
                        Spacer(modifier = Modifier.height(0.dp))
                        Text(
                            text = "Categoria: ${categoria.nome}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val textoPrazo = when {
                tarefa.concluida -> "Essa tarefa ja foi concluida"
                tarefa.prazoDias <= 0 -> "O prazo dessa tarefa e hoje"
                tarefa.prazoDias == 1 -> "Falta 1 dia para o prazo"
                else -> "Faltam ${tarefa.prazoDias} dias para o prazo"
            }
            Text(text = textoPrazo, style = MaterialTheme.typography.bodyMedium)
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
