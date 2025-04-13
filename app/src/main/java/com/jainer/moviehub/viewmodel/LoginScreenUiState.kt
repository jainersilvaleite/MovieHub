package com.jainer.moviehub.viewmodel

import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType

// ESTADOS DA LOGINSCREEN E SEUS MÉTODOS PARA ALTERAÇÃO
data class LoginScreenUiState(
    // campos para preenchimento do usuário
    val email: String = "",
    val password: String = "",
    // controle de eventos da tela
    val isLoading: Boolean = false,
    val authError: AppError = AppError(AppErrorType.UNKNOWN_ERROR),
    val authSuccess: AppSuccess = AppSuccess(AppSuccessType.UNKNOWN_SUCCESS),
    // métodos de modificação dos estados
    val onChangeEmail: (String) -> Unit = {},
    val onChangePassword: (String) -> Unit = {},
    val onChangeIsLoading: (Boolean) -> Unit = {},
    val onChangeAuthError: (AppError) -> Unit = {},
    val onChangeAuthSuccess: (AppSuccess) -> Unit = {}
)