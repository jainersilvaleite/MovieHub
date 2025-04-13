package com.jainer.moviehub.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.repository.AuthRepository
import com.jainer.moviehub.model.repository.MovieRepository
import com.jainer.moviehub.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DownloadsScreenViewModel: ViewModel() {
    // referenciando o repositório com os métodos de autenticação
    private val _authRepository = AuthRepository()
    // referenciando o repositório com os métodos para manuseio dos filmes do aplicativo
    private val _movieRepository = MovieRepository()
    // referenciando o repositório com os métodos para manuseio dos usuários do aplicativo
    private val _userRepository = UserRepository()
    // referenciando os estados da DownloadsScreen
    private val _uiState = MutableStateFlow(DownloadsScreenUiState())
    // expondo os estados da DownloadsScreen para a interface (view)
    val uiState = _uiState.asStateFlow()

    // DEFININDO OS MÉTODOS DO UISTATE DA DOWNLOADSSCREEN NA INICIALIZAÇÃO DA VIEWMODEL
    init {
        _uiState.update {
            it.copy(
                // alteração do estado que armazena a busca de filmes feita pelo usuário
                onChangeSearch = { search ->
                    _uiState.value = _uiState.value.copy(search = search)
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
                // alteração do estado que armazena o nome do atual usuário do aplicativo
                onChangeUsername = { username ->
                    _uiState.value = _uiState.value.copy(username = username)
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
        viewModelScope.launch(Dispatchers.IO) {
            _movieRepository.getSavedMovies(
                onAppSuccess = { appSuccess -> _uiState.value.onChangeAppSuccess(appSuccess) },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun getMoviesBySearch(search: String) {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch(Dispatchers.IO) {
            _movieRepository.getSavedMoviesBySearch(
                search = search,
                onAppSuccess = { appSuccess -> _uiState.value.onChangeAppSuccess(appSuccess) },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
            )
        }
    }

    fun deleteMovie(movieId: String, context: Context) {
        _uiState.value.onChangeIsLoading(true) // <-- inicializa o carregamento
        viewModelScope.launch(Dispatchers.IO) {
            _movieRepository.deleteMovie(
                movieId = movieId,
                onAppSuccess = { appSuccess ->
                    Toast.makeText(context, appSuccess.message, Toast.LENGTH_SHORT).show()
                },
                onAppError = { appError -> _uiState.value.onChangeAppError(appError) }
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
}