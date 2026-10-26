package com.exemplo.tarefasapp.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.exemplo.tarefasapp.navegacao.Rotas

@Composable
fun BottomBar(navController: NavController, rotaAtual: String) {
    NavigationBar {
        NavigationBarItem(
            selected = rotaAtual == Rotas.HOME,
            onClick = { navController.navigate(Rotas.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rotaAtual == Rotas.LISTA_TAREFAS,
            onClick = { navController.navigate(Rotas.LISTA_TAREFAS) },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Tarefas") },
            label = { Text("Tarefas") }
        )
        NavigationBarItem(
            selected = rotaAtual == Rotas.LISTA_CATEGORIAS,
            onClick = { navController.navigate(Rotas.LISTA_CATEGORIAS) },
            icon = { Icon(Icons.Default.Category, contentDescription = "Categorias") },
            label = { Text("Categorias") }
        )
        NavigationBarItem(
            selected = rotaAtual == Rotas.PERFIL,
            onClick = { navController.navigate(Rotas.PERFIL) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}
