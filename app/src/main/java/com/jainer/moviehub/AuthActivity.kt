package com.jainer.moviehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jainer.moviehub.model.repository.authCredentialProvider
import com.jainer.moviehub.model.repository.navigateToActivity
import com.jainer.moviehub.view.navigation.AuthNavigation
import com.jainer.moviehub.view.theme.MovieHubTheme

/* ACTIVITY PARA AUTENTICAÇÃO DO USUÁRIO (LOGIN/CADASTRO)
* (!) Ao abrir o aplicativo, o usuário acessará esta Activity, caso não esteja logado.
* Se o usuário já estiver logado, ele acessará a MainActivity (conteúdo do aplicativo) */
class AuthActivity: ComponentActivity() {
    // define previamente a variável que armazenará os serviços de autenticação do Firebase
    private lateinit var auth: FirebaseAuth
    // variável que armazenará os serviços de armazenamento do Firebase Realtime Database
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // SERVIÇOS DE AUTENTICAÇÃO DO FIREBASE AUTHENTICATION
        auth = FirebaseAuth.getInstance()
        // SERVIÇOS DE ARMAZENAMENTO DO FIREBASE REALTIME DATABASE
        databaseReference = FirebaseDatabase.getInstance().getReference("MovieHub")

        /* VERIFICA O LOGIN DO USUÁRIO
        * (!) Direciona o usuário para MainActivity se estiver logado.
        * Se não, ele permanece na AuthActivity para realizar sua autenticação */
        // coleta o usuário autenticado (se houver)
        val currentUser = auth.currentUser

        if (currentUser != null) { // <-- USUÁRIO AUTENTICADO
            // direciona o usuário para a MainActivity
            navigateToActivity(MainActivity::class.java, this)
        } else { // <-- USUÁRIO NÃO AUTENTICADO
            // direciona o usuário para a LoginScreen
            setContent {
                MovieHubTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        // INTERFACE (UI) QUE CONTROLA A NAVEGAÇÃO ENTRE TELAS DE AUTENTICAÇÃO
                        AuthNavigation(
                            authCredential = authCredentialProvider(
                                auth, databaseReference, this
                            ), modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}