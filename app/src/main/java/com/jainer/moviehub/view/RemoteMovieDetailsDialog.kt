package com.jainer.moviehub.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.google.firebase.auth.FirebaseAuth
import com.jainer.moviehub.R
import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.remote.Movie
import com.jainer.moviehub.view.util.MediumText
import com.jainer.moviehub.view.util.SmallText
import com.jainer.moviehub.view.util.TitleText
import java.time.Instant
import java.util.Date

// DIÁLOGO QUE EXIBE AS INFORMAÇÕES E COMENTÁRIOS DE UM FILME REMOTO ESPECÍFICO
@Composable
fun RemoteMovieDetailsDialog(
    auth: FirebaseAuth,
    username: String,
    movie: Movie,
    comments: List<Comment>,
    commentsQtt: Long,
    commentValue: String,
    onCommentSend: (Comment) -> Unit,
    onCommentValueChange: (String) -> Unit,
    onSaveOffline: () -> Unit,
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
                .fillMaxSize()
                .background(
                    color = Color(70, 30, 145),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(10.dp)
        ) {
            // OPÇÕES DO DIÁLOGO
            RemoteMovieDetailsDialogOptions(
                onSaveOffline = onSaveOffline,
                onDismissRequest = onDismissRequest
            )
            Spacer(modifier = Modifier.height(15.dp))
            // INFORMAÇÕES DO FILME (POSTER, AVALIAÇÕES, QTD. COMENTÁRIOS, ETC.)
            RemoteMovieDetailsDialogInfo(
                movie = movie,
                commentsQtt = commentsQtt
            )
            Spacer(modifier = Modifier.height(15.dp))
            MovieDetailsDialogComments(
                auth = auth,
                username = username,
                comments = comments,
                commentValue = commentValue,
                onCommentSend = onCommentSend,
                onCommentValueChange = onCommentValueChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RemoteMovieDetailsDialogOptions(
    onSaveOffline: () -> Unit,
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
            onClick = onSaveOffline,
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
                    imageVector = Icons.Default.Star,
                    contentDescription = "Salvar para ler offline",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                SmallText(
                    text = "Salvar offline",
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
fun RemoteMovieDetailsDialogInfo(
    movie: Movie,
    commentsQtt: Long,
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
            RemoteMovieDetailsDialogInfoField(
                fieldImageVector = Icons.Default.Star,
                fieldTitle = "Avaliação",
                fieldValue = movie.rating
            )
            // quantidade de comentários
            RemoteMovieDetailsDialogInfoField(
                fieldImageVector = Icons.Default.Person,
                fieldTitle = "Comentários",
                fieldValue = commentsQtt.toString()
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
fun RemoteMovieDetailsDialogInfoField(
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

@Composable
fun MovieDetailsDialogComments(
    auth: FirebaseAuth,
    username: String,
    comments: List<Comment>,
    commentValue: String,
    onCommentSend: (Comment) -> Unit,
    onCommentValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // SEÇÃO DE COMENTÁRIOS
    TitleText(
        text = "Comentários",
        textAlign = TextAlign.Center,
        color = Color.White
    )
    Spacer(modifier = Modifier.height(15.dp))
    // exibe todos os comentários deste filme
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .background(
                color = Color(137, 62, 255),
                shape = RoundedCornerShape(15.dp)
            ).fillMaxWidth()
    ) {
        if (comments.isNotEmpty()) {
            items(comments.size) { index ->
                MovieDetailsDialogComment(
                    auth = auth,
                    comment = comments[index]
                )
            }
        } else {
            item {
                MediumText(
                    text = "Não há comentários!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    // campo para escrita e envio de comentário
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = Color(137, 62, 255),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ) {
        TextField(
            value = commentValue,
            onValueChange = onCommentValueChange, // <--  onCommentValueChange
            placeholder = {
                Text("Insira seu comentário")
            },
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                onCommentSend(
                    Comment(
                        content = commentValue,
                        authorId = auth.uid!!,
                        authorName = username,
                        createdAt = Date.from(Instant.now())
                    )
                )
            }, // ação de enviar comentário
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar comentário",
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }
    }
}

@Composable
fun MovieDetailsDialogComment(
    auth: FirebaseAuth,
    comment: Comment,
    modifier: Modifier = Modifier
) {
    // VERIFICA SE O COMENTÁRIO FOI FEITO PELO USUÁRIO OU OUTRO EXISTENTE NO REALTIME DATABASE

    if (auth.uid == comment.authorId) {
        // conteúdo do comentário (feito pelo próprio usuário)
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 15.dp, vertical = 8.dp
                )
        ) {
            // nome do usuário
            Box(
                modifier = Modifier.background(
                    color = Color(70, 30, 145)
                )
            ) {
                MediumText(
                    text = "Você",
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            // comentário do usuário
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(70, 30, 145)
                    )
            ) {
                SmallText(
                    text = comment.content,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    } else {
        // conteúdo do comentário (feito por outro usuário)
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 15.dp, vertical = 8.dp
                )
        ) {
            // nome do usuário
            Box(
                modifier = Modifier.background(
                    color = Color(70, 30, 145)
                )
            ) {
                MediumText(
                    text = comment.authorName,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            // comentário do usuário
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(70, 30, 145)
                    )
            ) {
                SmallText(
                    text = comment.content,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }

}