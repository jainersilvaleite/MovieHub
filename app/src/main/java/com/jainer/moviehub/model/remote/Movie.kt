package com.jainer.moviehub.model.remote

// CLASSE DE DADOS QUE ARMAZENA AS INFORMAÇÕES DE UM FILME DA FAKEMOVIESAPI
data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val genre: List<String>,
    val rating: String,
    val plot: String,
    val runtime: String
)

/* CLASSE DE DADOS ORIGINAL (COM TODOS AS INFORMAÇÕES DO FILME)
data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val genre: List<String>,
    val rating: String,
    val director: String,
    val actors: List<String>,
    val plot: String,
    val poster: String,
    val trailer: String,
    val runtime: String,
    val awards: String,
    val country: String,
    val language: String,
    val boxOffice: String,
    val production: String,
    val website: String
)*/
