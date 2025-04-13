package com.jainer.moviehub.viewmodel

import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.remote.Movie
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType

data class HomeScreenUiState(
    // campos para preenchimento do usuário
    val search: String = "",
    val comment: String = "",
    // controle de eventos da tela
    val isLoading: Boolean = false,
    val movieOnFocus: Movie? = null,
    val refreshObserver: Boolean = false,
    val isSearchDialogOpened: Boolean = false,
    val isMovieDetailsDialogOpened: Boolean = false,
    val appError: AppError = AppError(AppErrorType.UNKNOWN_ERROR),
    val appSuccess: AppSuccess = AppSuccess(AppSuccessType.UNKNOWN_SUCCESS),
    // armazenamento de resultados
    val username: String = "usuário",
    val comments: List<Comment> = emptyList(),
    val commentsQtt: Long = 0,
    // métodos de modificação dos estados
    val onChangeSearch: (String) -> Unit = {},
    val onChangeComment: (String) -> Unit = {},
    val onChangeIsLoading: (Boolean) -> Unit = {},
    val onChangeMovieOnFocus: (Movie?) -> Unit = {},
    val onChangeRefreshObserver: () -> Unit = {},
    val onChangeIsSearchDialogOpened: (Boolean) -> Unit = {},
    val onChangeIsMovieDetailsDialogOpened: (Boolean) -> Unit = {},
    val onChangeAppError: (AppError) -> Unit = {},
    val onChangeAppSuccess: (AppSuccess) -> Unit = {},
    val onChangeUsername: (String) -> Unit = {},
    val onChangeComments: (List<Comment>) -> Unit = {},
    val onChangeCommentsQtt: (Long) -> Unit = {}
)
