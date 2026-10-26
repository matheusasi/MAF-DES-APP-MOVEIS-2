package com.exemplo.tarefasapp.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SobreScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sobre o App") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Tarefas App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Esse app foi feito para o Trabalho 2 de Desenvolvimento de Aplicativos Moveis. A ideia e organizar tarefas do dia a dia separadas por categorias."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Versao 1.0")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Essa tela veio do Trabalho 1, mas antes ela so existia como mockup estatico. Agora ela faz parte da navegacao de verdade do app."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SobreScreenPreview() {
    TarefasAppTheme {
        SobreScreen(navController = rememberNavController())
    }
}
