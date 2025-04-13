package com.jainer.moviehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.jainer.moviehub.model.repository.AuthRepository
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.model.util.AuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignupScreenViewModel: ViewModel() {
    // referenciando o repositório com os métodos de autenticação
    private val _authRepository = AuthRepository()
    // referenciando os estados da SignupScreen
    private val _uiState = MutableStateFlow(SignupScreenUiState())
    // expondo os estados da SignupScreen para a interface (view)
    val uiState = _uiState.asStateFlow()

    // DEFININDO OS MÉTODOS DO UISTATE DA LOGINSCREEN NA INICIALIZAÇÃO DA VIEWMODEL
    init {
        _uiState.update {
            it.copy(
                // alteração do estado que armazena o nome do usuário
                onChangeUsername = { username ->
                    _uiState.value = _uiState.value.copy(username = username)
                },
                // alteração do estado que armazena o e-mail do usuário
                onChangeEmail = { email ->
                    _uiState.value = _uiState.value.copy(email = email)
                },
                // alteração do estado que armazena a senha do usuário
                onChangePassword = { password ->
                    _uiState.value = _uiState.value.copy(password = password)
                },
                // alteração do estado que armazena a confirmação da senha do usuário
                onChangeRepeatedPassword = { repeatedPassword ->
                    _uiState.value = _uiState.value.copy(repeatedPassword = repeatedPassword)
                },
                // alteração do estado que monitora o carregamento do login
                onChangeIsLoading = { isLoading ->
                    _uiState.value = _uiState.value.copy(isLoading = isLoading)
                },
                // alteração do estado que armazena o erro do aplicativo
                onChangeAuthError = { authError ->
                    _uiState.value = _uiState.value.copy(authError = authError)
                },
                // alteração do estado que armazena o sucesso de uma tarefa do aplicativo
                onChangeAuthSuccess = { authSuccess ->
                    _uiState.value = _uiState.value.copy(authSuccess = authSuccess)
                }
            )
        }
    }

    // MÉTODOS DE COMUNICAÇÃO COM O REPOSITÓRIO (AUTHREPOSITORY) DA CAMADA MODEL
    fun signup(authCredential: AuthCredential, navController: NavController) {
        /* REALIZA O CADASTRO DO USUÁRIO COM AS CREDENCIAIS INFORMADAS
       * (!) Em caso de sucesso, atualizar o estado para expô-lo à camada View (UI)
       * (!) Em caso de erro, atualizar o estado para expô-lo à camada View (UI) */
        _authRepository.signup(
            authCredential = authCredential,
            navController = navController,
            onAppSuccess = { appSuccess ->
                _uiState.value.onChangeAuthSuccess(appSuccess)
                _uiState.value.onChangeAuthError(AppError(AppErrorType.UNKNOWN_ERROR))
            },
            onAppError = { appError ->
                _uiState.value.onChangeAuthError(appError)
                _uiState.value.onChangeAuthSuccess(AppSuccess(AppSuccessType.UNKNOWN_SUCCESS))
            }
        )
    }
}