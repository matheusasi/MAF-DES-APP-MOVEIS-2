package com.exemplo.tarefasapp.telas

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.componentes.BottomBar
import com.exemplo.tarefasapp.dados.AppDatabase
import com.exemplo.tarefasapp.navegacao.Rotas
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    var tarefasPendentes by remember { mutableStateOf(0) }
    var totalCategorias by remember { mutableStateOf(0) }

    // Os numeros do resumo vem do banco
    LaunchedEffect(Unit) {
        try {
            tarefasPendentes = db.tarefaDao().buscarTodos().count { !it.concluida }
            totalCategorias = db.categoriaDao().buscarTodos().size
        } catch (e: Exception) {
            Log.e("Home", "Erro ao buscar dados do banco", e)
        }
    }

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
                    Text(text = "$totalCategorias categoria(s) cadastrada(s)")
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
