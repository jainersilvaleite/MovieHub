package com.jainer.moviehub.viewmodel

import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType

// ESTADOS DA SIGNUPSCREEN E SEUS MÉTODOS PARA ALTERAÇÃO
data class SignupScreenUiState(
    // campos para preenchimento do usuário
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    // controle de eventos da tela
    val isLoading: Boolean = false,
    val authError: AppError = AppError(AppErrorType.UNKNOWN_ERROR),
    val authSuccess: AppSuccess = AppSuccess(AppSuccessType.UNKNOWN_SUCCESS),
    // métodos de modificação dos estados
    val onChangeUsername: (String) -> Unit = {},
    val onChangeEmail: (String) -> Unit = {},
    val onChangePassword: (String) -> Unit = {},
    val onChangeRepeatedPassword: (String) -> Unit = {},
    val onChangeIsLoading: (Boolean) -> Unit = {},
    val onChangeAuthError: (AppError) -> Unit = {},
    val onChangeAuthSuccess: (AppSuccess) -> Unit = {}
)
