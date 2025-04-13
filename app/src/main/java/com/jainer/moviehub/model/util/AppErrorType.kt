package com.jainer.moviehub.model.util

// CLASSE ENUMERADA QUE DISPÕE OS TIPOS DE ERRO ESPERADOS PELO APLICATIVO
enum class AppErrorType {
    AUTH_ERROR, // <-- erros de autenticação do usuário (login/cadastro)
    AUTH_CREDENTIAL_ERROR, // <-- erros de preenchimento indevido de credenciais de autenticação
    FAKE_MOVIES_API_ERROR, // <-- erros da Fake Movies API
    MOVIE_LOCAL_DB_ERROR, // <-- erros do banco de dados local para filmes salvos offline
    FIREBASE_DB_ERROR, // <-- erros do banco de dados fornecido pelo Firebase Realtime Database
    UNKNOWN_ERROR // <-- erros de origem desconhecida
}