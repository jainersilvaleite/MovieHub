package com.jainer.moviehub.model.util

import androidx.compose.runtime.Composable
import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.remote.User

/* CLASSE DE DADOS RESPONSÁVEL POR DESCREVER SUCESSOS PREVISTOS PELO APLICATIVO
* (!) Criada para facilitar o uso das informações relacionadas a sucessos de regras de negócio
* impostas pelo aplicativo na camada View, interagindo com a interface por meio de estados */
data class AppSuccess(
    val successType: AppSuccessType,
    val message: String = "",
    // armazenamento de resultados
    val movieApiResult: List<com.jainer.moviehub.model.remote.Movie> = emptyList(),
    val movieDbResult: List<com.jainer.moviehub.model.local.Movie> = emptyList(),
    val userResult: User = User(),
    val commentsResult: List<Comment> = emptyList(),
    val commentsQttResult: Long = 0,
    // funções de execução para quando o sucesso for disparado
    val onSuccess: () -> Unit = {},
    val onSuccessCompose: @Composable () -> Unit = {},
    val onSuccessSuspend: suspend () -> Unit = {}
)
