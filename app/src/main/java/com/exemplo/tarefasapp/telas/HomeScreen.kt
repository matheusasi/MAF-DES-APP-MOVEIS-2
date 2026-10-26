package com.exemplo.tarefasapp.telas

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.componentes.BottomBar
import com.exemplo.tarefasapp.dados.AppDados
import com.exemplo.tarefasapp.navegacao.Rotas
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@Composable
fun HomeScreen(navController: NavController) {
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
                text = "Ola!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Isso e um resumo do seu organizador de tarefas",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            val tarefasPendentes = AppDados.tarefas.count { !it.concluida }

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rotas.LISTA_TAREFAS) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Tarefas pendentes", style = MaterialTheme.typography.titleMedium)
                    Text(text = "$tarefasPendentes tarefa(s) ainda por fazer")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Rotas.LISTA_CATEGORIAS) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Categorias", style = MaterialTheme.typography.titleMedium)
                    Text(text = "${AppDados.categorias.size} categoria(s) cadastrada(s)")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TarefasAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
