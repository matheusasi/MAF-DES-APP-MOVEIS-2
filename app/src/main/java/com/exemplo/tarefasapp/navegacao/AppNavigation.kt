package com.exemplo.tarefasapp.navegacao

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exemplo.tarefasapp.telas.DetalheCategoriaScreen
import com.exemplo.tarefasapp.telas.DetalheTarefaScreen
import com.exemplo.tarefasapp.telas.HomeScreen
import com.exemplo.tarefasapp.telas.ListaCategoriasScreen
import com.exemplo.tarefasapp.telas.ListaTarefasScreen
import com.exemplo.tarefasapp.telas.PerfilScreen
import com.exemplo.tarefasapp.telas.SobreScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rotas.HOME) {
        composable(Rotas.HOME) {
            HomeScreen(navController)
        }
        composable(Rotas.LISTA_TAREFAS) {
            ListaTarefasScreen(navController)
        }
        composable(
            route = Rotas.DETALHE_TAREFA,
            arguments = listOf(navArgument("tarefaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tarefaId = backStackEntry.arguments?.getString("tarefaId")?.toIntOrNull() ?: 0
            DetalheTarefaScreen(navController, tarefaId)
        }
        composable(Rotas.LISTA_CATEGORIAS) {
            ListaCategoriasScreen(navController)
        }
        composable(
            route = Rotas.DETALHE_CATEGORIA,
            arguments = listOf(navArgument("categoriaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
            DetalheCategoriaScreen(navController, categoriaId)
        }
        composable(Rotas.PERFIL) {
            PerfilScreen(navController)
        }
        composable(Rotas.SOBRE) {
            SobreScreen(navController)
        }
    }
}
