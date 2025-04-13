package com.jainer.moviehub.model.util

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.time.Instant
import java.util.Date

// CLASSE DE DADOS QUE ATUARÁ COMO PARÂMETRO PARA FUNÇÕES/MÉTODOS DE AUTENTICAÇÃO
data class AuthCredential(
    // informações essenciais para autenticação e conexão da View com as demais camadas
    val auth: FirebaseAuth,
    val databaseReference: DatabaseReference? = null,
    val viewModelStoreOwner: ViewModelStoreOwner? = null,
    val context: Context? = null,
    // informações providenciadas pelo usuário na autenticação
    val username: String = "",
    val email: String =  "",
    val password: String = "",
    val repeatedPassword: String = "",
    // informações geradas pela requisição de autenticação do usuário
    val createdAt: Date = Date.from(Instant.now()) // <-- data e hora da requisição
)
