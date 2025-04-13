package com.jainer.moviehub.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.view.DownloadsScreen
import com.jainer.moviehub.view.HomeScreen
import com.jainer.moviehub.viewmodel.DownloadsScreenViewModel
import com.jainer.moviehub.viewmodel.HomeScreenViewModel

// INTERFACE (UI) QUE CONTROLA A NAVEGAÇÃO ENTRE AS PRINCIPAIS TELAS DO APLICATIVO
@Composable
fun AppNavigation(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference,
    homeScreenViewModel: HomeScreenViewModel,
    downloadsScreenViewModel: DownloadsScreenViewModel,
    modifier: Modifier = Modifier
) {
    /* CONTROLE DA NAVEGAÇÃO ENTRE AS TELAS DA MAINACTIVITY
    * (!) Destino inicial: HomeScreen
    * (!) Destinos possíveis:
    *       - HomeScreen
    *       - SearchScreen
    *       - DownloadsScreen */
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoutes.homeScreen
    ) {
        // redirecionamento para HomeScreen
        composable(AppRoutes.homeScreen) {
            HomeScreen(
                auth = auth,
                databaseReference = databaseReference,
                navController = navController,
                viewModel = homeScreenViewModel,
                modifier = modifier
            )
        }
        // redirecionamento para DownloadsScreen
        composable(AppRoutes.downloadsScreen) {
            DownloadsScreen(
                auth = auth,
                databaseReference = databaseReference,
                navController = navController,
                viewModel = downloadsScreenViewModel,
                modifier = modifier
            )
        }
    }
}