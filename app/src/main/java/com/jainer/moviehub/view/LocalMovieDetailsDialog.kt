package com.jainer.moviehub.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.rememberAsyncImagePainter
import com.jainer.moviehub.R
import com.jainer.moviehub.model.local.Movie
import com.jainer.moviehub.view.util.MediumText
import com.jainer.moviehub.view.util.SmallText
import com.jainer.moviehub.view.util.TitleText

// DIÁLOGO QUE EXIBE AS INFORMAÇÕES E COMENTÁRIOS DE UM FILME REMOTO ESPECÍFICO
@Composable
fun LocalMovieDetailsDialog(
    movie: Movie,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color(70, 30, 145),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp)
        ) {
            // OPÇÕES DO DIÁLOGO
            LocalMovieDetailsDialogOptions(
                onDelete = onDelete,
                onDismissRequest = onDismissRequest
            )
            Spacer(modifier = Modifier.height(15.dp))
            // INFORMAÇÕES DO FILME (POSTER, AVALIAÇÕES, QTD. COMENTÁRIOS, ETC.)
            LocalMovieDetailsDialogInfo(
                movie = movie
            )
        }
    }
}

@Composable
fun LocalMovieDetailsDialogOptions(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        // SALVAR INFORMAÇÕES DO FILME PARA VER OFFLINE
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .background(
                    color = Color(137, 62, 255),
                    shape = RoundedCornerShape(15.dp)
                )
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover filme",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                SmallText(
                    text = "Remover filme",
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
            }
        }
        // FECHAR O DIÁLOGO ATUAL
        IconButton(
            onClick = onDismissRequest
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar informações do filme",
                tint = Color.White
            )
        }
    }
}

@Composable
fun LocalMovieDetailsDialogInfo(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    // poster, avaliação e quantidade de comentários
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // poster
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
            modifier = Modifier
                .height(180.dp)
                .width(130.dp)
        )
        Spacer(modifier = Modifier.width(15.dp)) // <-- espaço entre poster e informações
        // avaliação e quantidade de comentários
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.height(180.dp)
        ) {
            // avaliação
            LocalMovieDetailsDialogInfoField(
                fieldImageVector = Icons.Default.Star,
                fieldTitle = "Avaliação",
                fieldValue = movie.rating
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    // outras informações do filme
    TitleText(
        text = movie.title,
        textAlign = TextAlign.Center,
        color = Color.White
    )
    SmallText(
        text = "Gênero(s): " + movie.genre.joinToString(", "),
        textAlign = TextAlign.Center,
        color = Color.White
    )
    SmallText(
        text = "Duração (minutos): " + movie.runtime,
        textAlign = TextAlign.Center,
        color = Color.White
    )
    Spacer(modifier = Modifier.height(15.dp))
    SmallText(
        text = movie.plot,
        textAlign = TextAlign.Center,
        color = Color.White
    )
}

@Composable
fun LocalMovieDetailsDialogInfoField(
    fieldImageVector: ImageVector,
    fieldTitle: String,
    fieldValue: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = fieldImageVector,
                contentDescription = fieldTitle,
                tint = Color.Yellow,
                modifier = Modifier.size(20.dp)
            )
            MediumText(
                text = fieldTitle,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        MediumText(
            text = fieldValue,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}