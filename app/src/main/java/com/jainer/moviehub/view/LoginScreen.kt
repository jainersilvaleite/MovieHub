package com.jainer.moviehub.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jainer.moviehub.AuthActivity
import com.jainer.moviehub.MainActivity
import com.jainer.moviehub.model.repository.authCredentialProvider
import com.jainer.moviehub.model.repository.clearAuthCredentials
import com.jainer.moviehub.model.repository.navigateToActivity
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.view.navigation.AuthRoutes
import com.jainer.moviehub.view.util.CredentialTextField
import com.jainer.moviehub.view.util.MediumText
import com.jainer.moviehub.view.util.SmallText
import com.jainer.moviehub.view.util.TitleText
import com.jainer.moviehub.viewmodel.LoginScreenViewModel

// TELA DE LOGIN DO APLICATIVO
@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel = viewModel()
) {
    // ESTADOS DA LOGINSCREEN TRAZIDOS DE SUA VIEWMODEL
    val uiState by viewModel.uiState.collectAsState()
    // CONTEXTO LOCAL (PARA EMISSÃO DE TOASTS)
    val context = LocalContext.current

    // COLUNA ONDE TODOS OS ELEMENTOS DA LOGINSCREEN ESTARÃO DISPOSTOS
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        // instruções de login
        TitleText(text = "Login", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        MediumText(
            text = "Entre com sua conta no MovieHub para ter acesso ao nosso catálogo de filmes.",
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
        )
        // espaçamento entre as instruções e os campos para preenchimento de dados
        Spacer(modifier = Modifier.height(50.dp))
        // campos para preenchimento de dados de login
        CredentialTextField( // <-- e-mail válido (verificado pelo Firebase Authentication)
            value = uiState.email,
            onValueChange = { uiState.onChangeEmail(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Seu e-mail") },
            maxLength = 255
        )
        Spacer(modifier = Modifier.height(10.dp)) // <-- espaçamento entre os campos
        CredentialTextField( // <-- no mínimo 6 caracteres, 1 número e 1 caractere especial
            value = uiState.password,
            onValueChange = { uiState.onChangePassword(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Sua senha") },
            maxLength = 16
        )
        Spacer(modifier = Modifier.height(10.dp)) // <-- espaçamento para o botão de login
        // botão de login
        OutlinedButton(
            onClick = {
                uiState.onChangeIsLoading(true) // <-- inicia o carregamento
                viewModel.login(
                    authCredentialProvider(
                        auth = auth,
                        context = context,
                        loginScreenUiState = uiState
                    )
                )
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (uiState.isLoading) "Carregando..." else "Fazer login")
        }
        Spacer(modifier = Modifier.height(50.dp))
        // intrução para opções alternativas
        SmallText(text = "Ou", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        // divisor da instrução e as opções disponíveis
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(bottom = 10.dp))
        // botão de cadastro
        OutlinedButton(
            onClick = { navController.navigate(AuthRoutes.signupScreen) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cadastrar-se")
        }
    }

    /* VERIFICA SE O USUÁRIO CONSEGUIU FAZER LOGIN
    * (!) Em caso de sucesso, limpar todos os campos de credenciais
    * e finalizar o processo de carregamento do aplicativo */
    val authSuccess = uiState.authSuccess // <-- disparo de sucesso de uma tarefa do aplicativo
    if (authSuccess.successType == AppSuccessType.LOGIN_SUCCESS) {
        Toast.makeText(context, authSuccess.message, Toast.LENGTH_SHORT).show()
        clearAuthCredentials(loginScreenUiState = uiState) // <-- limpa as credenciais
        uiState.onChangeIsLoading(false) // <-- finaliza o carregamento
        uiState.onChangeAuthSuccess(AppSuccess(AppSuccessType.UNKNOWN_SUCCESS))
        navigateToActivity(MainActivity::class.java, context)
    }

    /* VERIFICA SE HOUVE ALGUM ERRO
    * (!) Em caso de erro, finalizar o processo de carregamento do aplicativo */
    val authError = uiState.authError // <-- disparo de erro do aplicativo
    if (authError.errorType != AppErrorType.UNKNOWN_ERROR) {
        Toast.makeText(context, authError.appMessage, Toast.LENGTH_SHORT).show()
        uiState.onChangeIsLoading(false) // <-- finaliza o carregamento
        uiState.onChangeAuthError(AppError(AppErrorType.UNKNOWN_ERROR))
    }
}