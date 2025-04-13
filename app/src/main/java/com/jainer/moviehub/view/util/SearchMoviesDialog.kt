package com.jainer.moviehub.view.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

// DIÁLOGO ONDE O USUÁRIO PODE INSERIR UMA PESQUISA POR FILMES
@Composable
fun SearchMoviesDialog(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(color = Color(137, 62, 255))
                    .padding(10.dp)
            ) {
                TextField(
                    value = searchValue,
                    onValueChange = onSearchValueChange,
                    placeholder = {
                        Text("Insira sua busca")
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onSearch,
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar filmes",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }
            }
            /* CAIXA QUE OCUPA O RESTANTE DA TELA 
            * (!) Trata-se de uma forma alternativa de fechar o diálogo */
            Box(
                modifier = Modifier.fillMaxSize().clickable { onDismissRequest() }
            ) {}
        }
    }
}