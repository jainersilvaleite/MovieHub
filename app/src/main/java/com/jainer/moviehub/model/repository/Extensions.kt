package com.jainer.moviehub.model.repository

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.model.util.AuthCredential
import com.jainer.moviehub.viewmodel.LoginScreenUiState
import com.jainer.moviehub.viewmodel.SignupScreenUiState

/* ADICIONANDO NOVA FUNCIONALIDADE PARA AS ACTIVITIES
* (!) Agora, é possível navegar de uma Activity para outra "nativamente"
* (!) É possível gerar as credenciais de autenticação para uso em funções */
fun navigateToActivity(activity: Class<*>, context: Context) {
    val activityIntent = Intent(context, activity)
    /* CONFIGURAÇÃO DO REDIRECIONAMENTO
    * (!) FLAG_ACTIVITY_NEW_TASK: cria uma nova pilha de atividades para a Activity de destino
    * (!) FLAG_ACTIVITY_CLEAR_TASK: limpa a pilha de atividades anterior */
    activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(activityIntent) // <-- direciona para a Activity desejada
}

/* ADICIONANDO NOVAS FUNCIONALIDADES PARA O AUTHREPOSITORY
* (!) Agora, é possível validar as credenciais do usuário quanto a critérios pré-estabelecidos */
fun isLoginCredentialValid(
    authCredential: AuthCredential,
    onAppSuccess: (AppSuccess) -> Unit,
    onAppError: (AppError) -> Unit
): Boolean {
    with(authCredential) {
        // 1. NENHUM DOS CAMPOS PODE SER VAZIO
        if (email.isBlank() || password.isBlank()) {
            onAppError(
                AppError(
                    errorType = AppErrorType.AUTH_CREDENTIAL_ERROR,
                    appMessage = "Os campos não podem ser vazios!"
                )
            )
            return false
        }
        // SE AS CREDENCIAIS ESTÃO DE ACORDO COM OS CRITÉRIOS
        onAppSuccess(
            AppSuccess(
                successType = AppSuccessType.AUTH_CREDENTIAL_SUCCESS,
                message = AppSuccessType.AUTH_CREDENTIAL_SUCCESS.name
            )
        )
        return true
    }
}

fun isSignupCredentialValid(
    authCredential: AuthCredential,
    onAppSuccess: (AppSuccess) -> Unit,
    onAppError: (AppError) -> Unit
): Boolean {
    with(authCredential) {
        // 1. NENHUM DOS CAMPOS PODE SER VAZIO
        if (
            username.isBlank() || email.isBlank()
            || password.isBlank() || repeatedPassword.isBlank()
        ) {
            onAppError(
                AppError(
                    errorType = AppErrorType.AUTH_CREDENTIAL_ERROR,
                    appMessage = "Os campos não podem ser vazios!"
                )
            )
            return false
        }
        // 2. A SENHA DEVE TER NO MÍNIMO 6 LETRAS, 1 NÚMERO E 1 CARACTERE ESPECIAL
        val rule = Regex("^(?=(.*[a-zA-Z]){6,})(?=.*[0-9])(?=.*[!@#\$%^&*()-+=]).*$")
        if (!rule.matches(password)) {
            onAppError(
                AppError(
                    errorType = AppErrorType.AUTH_CREDENTIAL_ERROR,
                    appMessage = "Senha deve ter ao menos 6 letras, 1 número e 1 caractere especial!"
                )
            )
            return false
        }
        // 3. OS CAMPOS DE SENHA DEVEM SER IGUAIS
        if (password != repeatedPassword) {
            onAppError(
                AppError(
                    errorType = AppErrorType.AUTH_CREDENTIAL_ERROR,
                    appMessage = "As senhas informadas devem ser iguais!"
                )
            )
            return false
        }
        // SE AS CREDENCIAIS ESTÃO DE ACORDO COM OS CRITÉRIOS
        onAppSuccess(
            AppSuccess(
                successType = AppSuccessType.AUTH_CREDENTIAL_SUCCESS,
                message = "As credenciais informadas estão de acordo com os critérios!"
            )
        )
        return true
    }
}

/* FUNÇÃO QUE GERA UMA INSTÂNCIA DA CLASSE AUTHCREDENTIAL
* (!) A instância gerada depende dos argumentos da função (onde ela está sendo utilizada) */
fun authCredentialProvider(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference? = null,
    viewModelStoreOwner: ViewModelStoreOwner? = null,
    context: Context? = null,
    loginScreenUiState: LoginScreenUiState? = null,
    signupScreenUiState: SignupScreenUiState? = null
): AuthCredential {
    return if (loginScreenUiState != null) { // <-- gera as credenciais COM os campos de login
        AuthCredential(
            auth = auth,
            context = context,
            email = loginScreenUiState.email,
            password = loginScreenUiState.password
        )
    } else if (signupScreenUiState != null) { // <-- gera as credenciais COM os campos de cadastro
        AuthCredential(
            auth = auth,
            databaseReference = databaseReference,
            context = context,
            username = signupScreenUiState.username,
            email = signupScreenUiState.email,
            password = signupScreenUiState.password,
            repeatedPassword = signupScreenUiState.repeatedPassword
        )
    } else { // <-- gera as credenciais SEM os campos de autenticação
        AuthCredential(
            auth = auth,
            databaseReference = databaseReference,
            viewModelStoreOwner = viewModelStoreOwner,
            context = context
        )
    }
}

/* FUNÇÃO QUE LIMPA TODOS OS DADOS INSERIDOS NOS CAMPOS DE TEXTO DAS TELAS DA AUTHACTIVITY
* (!) Seu funcionamento depende dos argumentos da função (onde ela está sendo utilizada) */
fun clearAuthCredentials(
    loginScreenUiState: LoginScreenUiState? = null,
    signupScreenUiState: SignupScreenUiState? = null
) {
    if (loginScreenUiState != null) { // <-- limpa todos os campos da LoginScreen
        with(loginScreenUiState) {
            onChangeEmail("")
            onChangePassword("")
        }
    }
    if (signupScreenUiState != null) { // <-- limpa todos os campos da SignupScreen
        with(signupScreenUiState) {
            onChangeUsername("")
            onChangeEmail("")
            onChangePassword("")
            onChangeRepeatedPassword("")
        }
    }
}