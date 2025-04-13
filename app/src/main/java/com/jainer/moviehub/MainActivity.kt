package com.jainer.moviehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jainer.moviehub.view.navigation.AppNavigation
import com.jainer.moviehub.view.theme.MovieHubTheme
import com.jainer.moviehub.view.util.MovieHubBottomAppBar
import com.jainer.moviehub.view.util.MovieHubTopAppBar
import com.jainer.moviehub.viewmodel.DownloadsScreenViewModel
import com.jainer.moviehub.viewmodel.HomeScreenViewModel

class MainActivity : ComponentActivity() {
    // define previamente a variável que armazenará os serviços de autenticação do Firebase
    private lateinit var auth: FirebaseAuth
    // variável que armazenará os serviços de armazenamento do Realtime Database
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // SERVIÇOS DE AUTENTICAÇÃO DO FIREBASE AUTHENTICATION
        auth = FirebaseAuth.getInstance()
        // SERVIÇOS DE ARMAZENAMENTO DO FIREBASE REALTIME DATABASE
        databaseReference = FirebaseDatabase.getInstance().getReference("MovieHub")

        /* PROVÊ OS DADOS NECESSÁRIOS PARA CONEXÃO COM DA VIEW COM AS DEMAIS CAMADAS
        * (!) Coleta, individualmente, as ViewModels de cada tela da MainActivity */
        // coleta a ViewModel da HomeScreen
        val homeScreenViewModel = ViewModelProvider(this)[HomeScreenViewModel::class.java]
        // coleta a ViewModel da DownloadsScreen
        val downloadsScreenViewModel = ViewModelProvider(this)[DownloadsScreenViewModel::class.java]

        setContent {
            MovieHubTheme {
                AppNavigation(
                    auth = auth,
                    databaseReference = databaseReference,
                    homeScreenViewModel = homeScreenViewModel,
                    downloadsScreenViewModel = downloadsScreenViewModel
                )
            }
        }
    }
}