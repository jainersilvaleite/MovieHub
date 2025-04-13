package com.jainer.moviehub.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.local.Movie
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.view.navigation.AppRoutes
import com.jainer.moviehub.view.util.MediumText
import com.jainer.moviehub.view.util.MovieHubBottomAppBar
import com.jainer.moviehub.view.util.MovieHubTopAppBar
import com.jainer.moviehub.view.util.RemoteMovieCard
import com.jainer.moviehub.view.util.SearchMoviesDialog
import com.jainer.moviehub.view.util.SmallText
import com.jainer.moviehub.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel()
) {
    // ESTADOS DA HOMESCREEN TRAZIDOS DE SUA VIEWMODEL
    val uiState by viewModel.uiState.collectAsState()
    // CONTEXTO LOCAL (PARA ENCERRAMENTO DE SESSÃO)
    val context = LocalContext.current

    // realiza a coleta do usuário logado atualmente no aplicativo
    LaunchedEffect(Unit) { auth.uid?.let { viewModel.getUser(it, databaseReference) } }

    // inicia a requisição dos filmes para a API Fake e a refaz quando o usuário atualiza a página
    LaunchedEffect(uiState.refreshObserver) { viewModel.getMovies() }

    Scaffold(
        topBar = {
            MovieHubTopAppBar(
                onRefresh = { uiState.onChangeRefreshObserver() },
                onSearch = { uiState.onChangeIsSearchDialogOpened(true) },
                onDownloadsScreenAction = { navController.navigate(AppRoutes.downloadsScreen) }
            )
        },
        bottomBar = {
            MovieHubBottomAppBar(
                username = uiState.username,
                onLogout = { viewModel.logout(auth, context) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        // EXIBINDO INFORMAÇÕES A PARTIR DO RESULTADO DA API FAKE
        if (uiState.isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize().padding(paddingValues)
            ) {
                CircularProgressIndicator()
            }
        }

        /* VERIFICA SE OS FILMES DA API FAKE FORAM CARREGADOS
        * (!) Em caso de sucesso, finalizar o processo de carregamento do aplicativo */
        val appSuccess = uiState.appSuccess // <-- disparo de sucesso de uma tarefa do aplicativo
        if (appSuccess.successType == AppSuccessType.FAKE_MOVIES_API_SUCCESS) {
            uiState.onChangeIsLoading(false) // <-- finaliza o carregamento

            val moviesList = appSuccess.movieApiResult // <-- coleta a lista de filmes carregados

            /* VERIFICA SE A LISTA DE FILMES CARREGADOS ESTÁ VAZIA
            * (!) Se estiver vazia, significa que a pesquisa do usuário não
            * encontrou resultados similares, retornando um aviso */
            if (moviesList.isNotEmpty()) {
                // GRID ONDE TODOS FILMES DA API FAKE ESTARÃO DISPOSTOS
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = modifier.fillMaxSize().padding(paddingValues)
                ) {
                    items(moviesList.size) { index ->
                        // FORMATA E ORGANIZA AS INFORMAÇÕES COLETADAS DO FILME
                        RemoteMovieCard(
                            movie = moviesList[index],
                            onClickMovieCard = {
                                uiState.onChangeMovieOnFocus(moviesList[index])
                                uiState.onChangeIsMovieDetailsDialogOpened(true)
                            }
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxSize().padding(paddingValues)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Busca não corresponde a nenhum filme!",
                        modifier = Modifier.size(50.dp)
                    )
                    MediumText(
                        text = "Sua pesquisa não encontrou nenhum filme!"
                    )
                }
            }

            uiState.onChangeAppError(AppError(AppErrorType.UNKNOWN_ERROR))
        }

        /* VERIFICA SE HOUVE ALGUM ERRO
        * (!) Em caso de erro, finalizar o processo de carregamento do aplicativo */
        val appError = uiState.appError // <-- disparo de erro do aplicativo
        if (appError.errorType != AppErrorType.UNKNOWN_ERROR) {
            uiState.onChangeIsLoading(false) // <-- finaliza o carregamento

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize().padding(paddingValues)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Erro ao carregar os filmes!",
                    modifier = Modifier.size(50.dp)
                )
                MediumText(
                    text = appError.appMessage
                )
                SmallText(
                    text = "Verifique sua conexão.",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
            }

            uiState.onChangeAppSuccess(AppSuccess(AppSuccessType.UNKNOWN_SUCCESS))
        }
    }

    // EXIBE O DIÁLOGO DE BUSCA DE FILMES DA HOMESCREEN
    if (uiState.isSearchDialogOpened) {
        SearchMoviesDialog(
            searchValue = uiState.search,
            onSearchValueChange = { uiState.onChangeSearch(it) },
            onSearch = {
                viewModel.getMoviesBySearch(uiState.search)
                uiState.onChangeSearch("")
                uiState.onChangeIsSearchDialogOpened(false)
            },
            onDismissRequest = { uiState.onChangeIsSearchDialogOpened(false) }
        )
    }

    // EXIBE O DIÁLOGO DE INFORMAÇÕES DO FILME DA HOMESCREEN
    if (uiState.isMovieDetailsDialogOpened) {
        LaunchedEffect(Unit) {
            uiState.movieOnFocus?.let { movieOnFocus ->
                viewModel.getCommentsQuantity(movieOnFocus.id, databaseReference)
                viewModel.getComments(movieOnFocus.id, databaseReference)
            }
        }

        RemoteMovieDetailsDialog(
            auth = auth,
            username = uiState.username,
            movie = uiState.movieOnFocus!!,
            comments = uiState.comments,
            commentsQtt = uiState.commentsQtt,
            commentValue = uiState.comment,
            onCommentSend = { comment ->
                viewModel.sendComment(
                    movieId = uiState.movieOnFocus!!.id,
                    comment = comment,
                    databaseReference = databaseReference
                )
                uiState.onChangeComment("")
                uiState.onChangeIsLoading(false) // <-- finaliza o carregamento
            },
            onCommentValueChange = { uiState.onChangeComment(it) },
            onSaveOffline = {
                uiState.movieOnFocus?.let {
                    viewModel.insertMovie(
                        Movie(
                            id = it.id,
                            title = it.title,
                            year = it.year,
                            genre = it.genre,
                            rating = it.rating,
                            plot = it.plot,
                            runtime = it.runtime
                        )
                    )
                    uiState.onChangeRefreshObserver()
                    uiState.onChangeIsMovieDetailsDialogOpened(false)
                    uiState.onChangeMovieOnFocus(null)
                }
            },
            onDismissRequest = {
                uiState.onChangeIsMovieDetailsDialogOpened(false)
                uiState.onChangeMovieOnFocus(null)
                uiState.onChangeComments(emptyList())
            }
        )
    }
}