package com.jainer.moviehub.view.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// BARRA INFERIOR DO APLICATIVO (TELAS DA MAINACTIVITY)
@Composable
fun MovieHubBottomAppBar(
    username: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        containerColor = Color(137, 62, 255, 255),
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        TitleText(
            text = "Bem-vindo, $username!",
            textAlign = TextAlign.Left,
            color = Color.White,
            modifier = modifier.weight(1f)
        )
        // BOTÃO PARA ENCERRAR A SESSÃO DO USUÁRIO ATUAL (LOGOUT)
        IconButton(
            onClick = onLogout
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Sair",
                tint = Color.White,
                modifier = modifier.scale(
                    scaleX = -1f, scaleY = 1f
                ) // <-- inverte o ícone horizontalmente
            )
        }
    }
}