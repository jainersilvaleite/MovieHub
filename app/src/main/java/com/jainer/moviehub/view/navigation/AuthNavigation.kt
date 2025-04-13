package com.jainer.moviehub.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainer.moviehub.model.util.AuthCredential
import com.jainer.moviehub.view.LoginScreen
import com.jainer.moviehub.view.SignupScreen
import com.jainer.moviehub.viewmodel.LoginScreenViewModel
import com.jainer.moviehub.viewmodel.SignupScreenViewModel

// INTERFACE (UI) QUE CONTROLA A NAVEGAÇÃO ENTRE TELAS DE AUTENTICAÇÃO
@Composable
fun AuthNavigation(
    authCredential: AuthCredential,
    modifier: Modifier = Modifier
) {
    /* PROVÊ OS DADOS NECESSÁRIOS PARA CONEXÃO COM DA VIEW COM AS DEMAIS CAMADAS
    * (!) Coleta, individualmente, as ViewModels de cada tela da AuthActivity */
    authCredential.viewModelStoreOwner?.let {
        // coleta a ViewModel da LoginScreen
        val loginScreenViewModel = ViewModelProvider(it)[LoginScreenViewModel::class.java]
        // coleta a ViewModel da SignupScreen
        val signupScreenViewModel = ViewModelProvider(it)[SignupScreenViewModel::class.java]

        /* CONTROLE DA NAVEGAÇÃO ENTRE AS TELAS DA AUTHACTIVITY
        * (!) Destino inicial: LoginScreen
        * (!) Destinos possíveis: LoginScreen e SignupScreen */
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = AuthRoutes.loginScreen
        ) {
            // redirecionamento para LoginScreen
            composable(AuthRoutes.loginScreen) {
                LoginScreen(
                    auth = authCredential.auth,
                    navController = navController,
                    modifier = modifier, viewModel = loginScreenViewModel
                )
            }
            // redirecionamento para SignupScreen
            composable(AuthRoutes.signupScreen) {
                SignupScreen(
                    auth = authCredential.auth,
                    databaseReference = authCredential.databaseReference,
                    navController = navController,
                    modifier = modifier, viewModel = signupScreenViewModel
                )
            }
        }
    }
}