package com.jainer.moviehub.model.remote

import java.time.Instant
import java.util.Date

// CLASSE DE DADOS QUE ARMAZENARÁ AS INFORMAÇÕES DO USUÁRIO LOGADO
data class User(
    val uid: String = "",
    val username: String = "",
    val createdAt: Date = Date.from(Instant.now())
)
