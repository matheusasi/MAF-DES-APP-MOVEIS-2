package com.exemplo.tarefasapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CoresClaras = lightColorScheme(
    primary = RoxoPrincipal,
    secondary = RoxoClaro,
    background = FundoClaro,
    onBackground = CinzaTexto
)

private val CoresEscuras = darkColorScheme(
    primary = RoxoClaro,
    secondary = RoxoPrincipal
)

@Composable
fun TarefasAppTheme(content: @Composable () -> Unit) {
    val esquemaDeCores = if (isSystemInDarkTheme()) CoresEscuras else CoresClaras

    MaterialTheme(
        colorScheme = esquemaDeCores,
        content = content
    )
}
