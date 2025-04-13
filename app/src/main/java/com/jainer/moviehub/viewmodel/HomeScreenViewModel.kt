package com.jainer.moviehub.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.local.Movie
import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.repository.AuthRepository
import com.jainer.moviehub.model.repository.MovieRepository
import com.jainer.moviehub.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {
    // referenciando o repositório com os métodos de autenticação
    private val _authRepository = AuthRepository()
    // referenciando o repositório com os métodos para manuseio dos filmes do aplicativo
    private val _movieRepository = MovieRepository()
    // referenciando o repositório com os métodos para manuseio dos usuários do aplicativo
    private val _userRepository = UserRepository()
    // referenciando os estados da HomeScreen
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    // expondo os estados da HomeScreen para a interface (view)
    val uiState = _uiState.asStateFlow()

    // DEFININDO OS MÉTODOS DO UISTATE DA HOMESCREEN NA INICIALIZAÇÃO DA VIEWMODEL
    init {
        _uiState.update {
            it.copy(
                // alteração do estado que armazena a busca de filmes feita pelo usuário
                onChangeSearch = { search ->
                    _uiState.value = _uiState.value.copy(search = search)
                },
                // alteração do estado que armazena o comentário de um filme feito pelo usuário
                onChangeComment = { comment ->
                    _uiState.value = _uiState.value.copy(comment = comment)
                },
                // alteração do estado que monitora o carregamento dos filmes
                onChangeIsLoading = { isLoading ->
                    _uiState.value = _uiState.value.copy(isLoading = isLoading)
                },
                // alteração do estado que monitora o filme selecionado recentemente
                onChangeMovieOnFocus = { movieOnFocus ->
                    _uiState.value = _uiState.value.copy(movieOnFocus = movieOnFocus)
                },
                // alteração do estado que monitora o recarregamento dos filmes
                onChangeRefreshObserver = {
                    // coleta o valor atual do estado para invertê-lo em seguida
                    val refreshObserver = _uiState.value.refreshObserver
                    _uiState.value = _uiState.value.copy(refreshObserver = !refreshObserver)
                },
                // alteração do estado que monitora a abertura/fechamento do diálogo de busca
                onChangeIsSearchDialogOpened = { isSearchDialogOpened ->
                    _uiState.value = _uiState.value.copy(isSearchDialogOpened = isSearchDialogOpened)
                },
                // alteração do estado que monitora a abertura/fechamento do diálogo do filme
                onChangeIsMovieDetailsDialogOpened = { isMovieDetailsDialogOpened ->
                    _uiState.value = _uiState.value.copy(
                        isMovieDetailsDialogOpened = isMovieDetailsDialogOpened
                    )
                },
                // alteração do estado que armazena o erro do aplicativo
                onChangeAppError = { appError ->
                    _uiState.value = _uiState.value.copy(appError = appError)
                },
                // alteração do estado que armazena o sucesso de uma tarefa do aplicativo
                onChangeAppSuccess = { appSuccess ->
                    _uiState.value = _uiState.value.copy(appSuccess = appSuccess)
                },
                // alteração do estado que armazena o nome do usuário atual do aplicativo
                onChangeUsername = { username ->
                    _uiState.value = _uiState.value.copy(username = username)
                },
                // alteração do estado que armazena os comentários do filme em foco atualmente
                onChangeComments = { comments ->
                    _uiState.value = _uiState.value.copy(comments = comments)
                },
                // alteração do estado que armazena a quantidade de comentários do filme em foco
                onChangeCommentsQtt = { commentsQtt ->
                    _uiState.value = _uiState.value.copy(commentsQtt = commentsQtt)
                },
            )
        }
    }

    // MÉTODOS DE COMUNICAÇÃO COM O REPOSITÓRIO (AUTHREPOSITORY) DA CAMADA MODEL
    fun logout(auth: FirebaseAuth, context: Context) {
        _authRepository.logout(auth, context)
    }

    // MÉTODOS DE COMUNICAÇÃO COM O REPOSITÓRIO (MOVIEREPOSITORY) DA CAMADA MODEL
    fun getMovies() {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch {
            _movieRepository.getMovies(
                onAppSuccess = { appSuccess -> _uiState.value.onChangeAppSuccess(appSuccess) },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun getMoviesBySearch(search: String) {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch {
            _movieRepository.getMoviesBySearch(
                search = search,
                onAppSuccess = { appSuccess -> _uiState.value.onChangeAppSuccess(appSuccess) },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun insertMovie(movie: Movie) {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch(Dispatchers.IO) {
            _movieRepository.insertMovie(
                movie = movie,
                onAppSuccess = { appSuccess -> _uiState.value.onChangeAppSuccess(appSuccess) },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun getComments(movieId: String, databaseReference: DatabaseReference) {
        viewModelScope.launch {
            _movieRepository.getComments(
                movieId = movieId,
                databaseReference = databaseReference,
                onAppSuccess = { appSuccess ->
                    _uiState.value.onChangeComments(appSuccess.commentsResult)
                }, onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun getCommentsQuantity(movieId: String, databaseReference: DatabaseReference) {
        viewModelScope.launch {
            _movieRepository.getCommentsQuantity(
                movieId = movieId,
                databaseReference = databaseReference,
                onAppSuccess = { appSuccess ->
                    _uiState.value.onChangeCommentsQtt(appSuccess.commentsQttResult)
                }, onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    // MÉTODOS DE COMUNICAÇÃO COM O REPOSITÓRIO (USERREPOSITORY) DA CAMADA MODEL
    fun getUser(userId: String, databaseReference: DatabaseReference) {
        viewModelScope.launch {
            _userRepository.getUser(
                userId = userId,
                databaseReference = databaseReference,
                onAppSuccess = { appSuccess ->
                    _uiState.value.onChangeUsername(appSuccess.userResult.username)
                }, onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun sendComment(movieId: String, comment: Comment, databaseReference: DatabaseReference) {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch {
            _userRepository.sendComment(
                movieId = movieId,
                comment = comment,
                databaseReference = databaseReference,
                onAppSuccess = {
                    _movieRepository.getComments(
                        movieId = movieId,
                        databaseReference = databaseReference,
                        onAppSuccess = { appSuccess ->
                            _uiState.value.onChangeComments(appSuccess.commentsResult)
                        }, onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
                    )
                }, onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }
}