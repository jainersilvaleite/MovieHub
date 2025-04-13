package com.jainer.moviehub.model.util

import androidx.compose.runtime.Composable

/* CLASSE DE DADOS RESPONSÁVEL POR DESCREVER ERROS PREVISTOS PELO APLICATIVO
* (!) Criada para facilitar o uso das informações relacionadas a erros de regras de negócio
* impostas pelo aplicativo na camada View, interagindo com a interface por meio de estados */
data class AppError(
    val errorType: AppErrorType,
    val message: String? = null,
    val cause: Throwable? = null,
    // mensagem do erro descrita do ponto de vista do aplicativo
    val appMessage: String = "",
    // causa do erro descrita do ponto de vista do aplicativo
    val appCause: String = "",
    // funções de execução para quando o erro for disparado
    val onError: () -> Unit = {},
    val onErrorCompose: @Composable () -> Unit = {},
    val onErrorSuspend: suspend () -> Unit = {}
)
