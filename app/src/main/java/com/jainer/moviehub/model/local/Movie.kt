package com.jainer.moviehub.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// CLASSE DE DADOS QUE ATUA COMO ENTIDADE DO BANCO DE DADOS DE FILMES
@Entity
data class Movie(
    @PrimaryKey
    val id: String,
    val title: String,
    val year: String,
    val genre: List<String>,
    val rating: String,
    val plot: String,
    val runtime: String
)
