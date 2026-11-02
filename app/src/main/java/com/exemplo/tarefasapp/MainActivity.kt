package com.exemplo.tarefasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.exemplo.tarefasapp.data.AppDatabase
import com.exemplo.tarefasapp.data.repository.CategoriaRepository
import com.exemplo.tarefasapp.data.repository.TarefaRepository
import com.exemplo.tarefasapp.ui.navegacao.AppNavigation
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // O banco e os Repositories sao criados aqui uma vez so e entregues para as telas
        val db = AppDatabase.getInstance(applicationContext)
        val tarefaRepository = TarefaRepository(db.tarefaDao())
        val categoriaRepository = CategoriaRepository(db.categoriaDao())

        setContent {
            TarefasAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(tarefaRepository, categoriaRepository)
                }
            }
        }
    }
}
