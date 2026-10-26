package com.exemplo.tarefasapp.telas

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.exemplo.tarefasapp.navegacao.Rotas
import com.exemplo.tarefasapp.ui.theme.TarefasAppTheme

@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    var nome by remember { mutableStateOf("Aluno de DAM") }

    Scaffold(
        bottomBar = { BottomBar(navController, Rotas.PERFIL) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Meu Perfil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                Toast.makeText(context, "Perfil atualizado", Toast.LENGTH_SHORT).show()
            }) {
                Text("Salvar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = { navController.navigate(Rotas.SOBRE) }) {
                Text("Sobre o App")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    TarefasAppTheme {
        PerfilScreen(navController = rememberNavController())
    }
}
