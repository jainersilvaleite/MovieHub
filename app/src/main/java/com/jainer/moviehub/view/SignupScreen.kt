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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.repository.authCredentialProvider
import com.jainer.moviehub.model.repository.clearAuthCredentials
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.view.navigation.AuthRoutes
import com.jainer.moviehub.view.util.CredentialTextField
import com.jainer.moviehub.view.util.MediumText
import com.jainer.moviehub.view.util.SmallText
import com.jainer.moviehub.view.util.TitleText
import com.jainer.moviehub.viewmodel.SignupScreenViewModel

// TELA DE CADASTRO DO APLICATIVO
@Composable
fun SignupScreen(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference?,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SignupScreenViewModel = viewModel()
) {
    // ESTADOS DA SIGNUPSCREEN TRAZIDOS DE SUA VIEWMODEL
    val uiState by viewModel.uiState.collectAsState()
    // CONTEXTO LOCAL (PARA EMISSÃO DE TOASTS)
    val context = LocalContext.current

    // COLUNA ONDE TODOS OS ELEMENTOS DA SIGNUPSCREEN ESTARÃO DISPOSTOS
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        // instruções de cadastro
        TitleText(text = "Cadastro", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        MediumText(
            text = "Crie sua conta no MovieHub e desfrute de nosso catálogo agora mesmo!",
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
        )
        // espaçamento entre as instruções e os campos para preenchimento de dados
        Spacer(modifier = Modifier.height(50.dp))
        // campos para preenchimento de dados de cadastro
        CredentialTextField( // <-- no máximo 20 caracteres
            value = uiState.username,
            onValueChange = { uiState.onChangeUsername(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Seu nome de usuário") },
            maxLength = 20
        )
        Spacer(modifier = Modifier.height(10.dp)) // <-- espaçamento entre os campos
        CredentialTextField( // <-- e-mail válido (verificado pelo Firebase Auth)
            value = uiState.email,
            onValueChange = { uiState.onChangeEmail(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Seu melhor e-mail") },
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
        Spacer(modifier = Modifier.height(10.dp)) // <-- espaçamento entre os campos
        CredentialTextField(
            value = uiState.repeatedPassword,
            onValueChange = { uiState.onChangeRepeatedPassword(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Repita sua senha") },
            maxLength = 16
        )
        Spacer(modifier = Modifier.height(10.dp)) // <-- espaçamento para o botão de cadastro
        // botão de cadastro
        OutlinedButton(
            onClick = {
                uiState.onChangeIsLoading(true) // <-- inicia o carregamento
                viewModel.signup(
                    authCredential = authCredentialProvider(
                        auth = auth,
                        databaseReference = databaseReference,
                        signupScreenUiState = uiState
                    ),
                    navController = navController
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Fazer cadastro")
        }
        Spacer(modifier = Modifier.height(50.dp))
        // intrução para opções alternativas
        SmallText(text = "Ou", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        // divisor da instrução e as opções disponíveis
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(bottom = 10.dp))
        // botão de login
        OutlinedButton(
            onClick = { navController.navigate(AuthRoutes.loginScreen) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Fazer login")
        }
    }

    /* VERIFICA SE O USUÁRIO CONSEGUIU FAZER O CADASTRO
    * (!) Em caso de sucesso, limpar todos os campos de credenciais
    * e finalizar o processo de carregamento do aplicativo */
    val authSuccess = uiState.authSuccess // <-- disparo de sucesso de uma tarefa do aplicativo
    if (authSuccess.successType == AppSuccessType.SIGNUP_SUCCESS) {
        Toast.makeText(context, authSuccess.message, Toast.LENGTH_SHORT).show()
        clearAuthCredentials(signupScreenUiState = uiState) // <-- limpa as credenciais
        uiState.onChangeIsLoading(false) // <-- finaliza o carregamento
        uiState.onChangeAuthSuccess(AppSuccess(AppSuccessType.UNKNOWN_SUCCESS))
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