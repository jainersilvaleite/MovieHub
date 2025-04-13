package com.jainer.moviehub.model.remote

import java.time.Instant
import java.util.Date

// CLASSE DE DADOS QUE ARMAZENARÁ AS INFORMAÇÕES DE CADA COMENTÁRIO DE UM FILME
data class Comment(
    val id: String = "",
    val content: String = "",
    val createdAt: Date = Date.from(Instant.now()),
    val authorId: String = "",
    val authorName: String = ""
)
