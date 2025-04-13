package com.jainer.moviehub.view.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

// BARRA SUPERIOR DO APLICATIVO (TELAS DA MAINACTIVITY)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieHubTopAppBar(
    onRefresh: () -> Unit,
    onSearch: () -> Unit,
    onDownloadsScreenAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                TitleText(
                    text = "MovieHub",
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
                SmallText(
                    text = "Seus filmes em um só lugar!",
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
            }
        },
        actions = {
            // BOTÃO PARA RECARREGAR OS FILMES DA API FAKE
            IconButton(
                onClick = onRefresh
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Recarregar"
                )
            }
            // BOTÃO PARA ABRIR O DIÁLOGO DE PESQUISA (SEARCHMOVIESDIALOG)
            IconButton(
                onClick = onSearch
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar"
                )
            }
            // BOTÃO PARA ACESSAR A TELA DE DOWNLOADS/FAVORITOS (DOWNLOADSSCREEN)
            IconButton(
                onClick = onDownloadsScreenAction
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Downloads"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(137, 62, 255, 255),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = modifier
    )
}