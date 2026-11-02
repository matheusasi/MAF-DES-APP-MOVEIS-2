package com.exemplo.tarefasapp.ui.navegacao

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.ui.DetalheCategoriaScreen
import com.exemplo.tarefasapp.ui.DetalheTarefaScreen
import com.exemplo.tarefasapp.ui.HomeScreen
import com.exemplo.tarefasapp.ui.ListaCategoriasScreen
import com.exemplo.tarefasapp.ui.ListaTarefasScreen
import com.exemplo.tarefasapp.ui.PerfilScreen
import com.exemplo.tarefasapp.ui.SobreScreen

@Composable
fun AppNavigation(
    tarefaRepository: TarefaRepository,
    categoriaRepository: CategoriaRepository
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rotas.HOME) {
        composable(Rotas.HOME) {
            HomeScreen(navController, tarefaRepository, categoriaRepository)
        }
        composable(Rotas.LISTA_TAREFAS) {
            ListaTarefasScreen(navController, tarefaRepository, categoriaRepository)
        }
        composable(
            route = Rotas.DETALHE_TAREFA,
            arguments = listOf(navArgument("tarefaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tarefaId = backStackEntry.arguments?.getString("tarefaId")?.toIntOrNull() ?: 0
            DetalheTarefaScreen(navController, tarefaId, tarefaRepository, categoriaRepository)
        }
        composable(Rotas.LISTA_CATEGORIAS) {
            ListaCategoriasScreen(navController, categoriaRepository, tarefaRepository)
        }
        composable(
            route = Rotas.DETALHE_CATEGORIA,
            arguments = listOf(navArgument("categoriaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull() ?: 0
            DetalheCategoriaScreen(navController, categoriaId, categoriaRepository, tarefaRepository)
        }
        composable(Rotas.PERFIL) {
            PerfilScreen(navController)
        }
        composable(Rotas.SOBRE) {
            SobreScreen(navController)
        }
    }
}
