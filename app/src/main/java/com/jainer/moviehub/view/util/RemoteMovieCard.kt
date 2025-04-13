package com.jainer.moviehub.view.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.jainer.moviehub.R
import com.jainer.moviehub.model.remote.Movie

// CARD QUE EXIBIRÁ AS INFORMAÇÕES DO FILME REMOTO ESPECIFICADO
@Composable
fun RemoteMovieCard(
    movie: Movie,
    onClickMovieCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        // exibe um diálogo contendo as informações e seção de comentários do filme
        onClick = onClickMovieCard,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(70, 30, 145)),
        elevation = CardDefaults.cardElevation(defaultElevation = 25.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    /* TENTA COLETAR A IMAGEM DO POSTER DA INTERNET
                    * (!) Enquanto estiver carregando, uma imagem placeholder assumirá
                    * a posição da imagem final (retirada da internet)
                    * (!) Se não for possível, exibir uma imagem local para contornar erros */
                    painter = rememberAsyncImagePainter(
                        model = "https://fakeimg.pl/220x310/893eff",
                        placeholder = painterResource(R.drawable.poster_placeholder),
                        error = painterResource(R.drawable.poster_error)
                    ),
                    contentDescription = "Poster do filme",
                    modifier = modifier.size(120.dp)
                )
                Spacer(modifier = modifier.height(5.dp)) // <-- espaço entre o poster e a avaliação
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    MediumText(
                        text = movie.rating,
                        textAlign = TextAlign.Left,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Avaliação do filme",
                        tint = Color.Yellow,
                        modifier = modifier.size(20.dp)
                    )
                }
            }
            // informações gerais do filme coletado da API Fake
            Column {
                Spacer(modifier = modifier.height(10.dp))
                TitleText(
                    text = movie.title + " (${movie.year})",
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
                SmallText(
                    text = "Gênero(s): " + movie.genre.joinToString(", "),
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
                SmallText(
                    text = "Duração (minutos): " + movie.runtime,
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
                Spacer(modifier = modifier.height(10.dp))
                MediumText(
                    text = movie.plot,
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
            }
        }
    }
}