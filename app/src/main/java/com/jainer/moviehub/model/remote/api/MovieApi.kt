package com.jainer.moviehub.model.remote.api

import com.jainer.moviehub.model.remote.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// INTERFACE QUE DÁ ACESSO ÀS OPERAÇÕES DA API FAKE DE FILMES
interface MovieApi {
    @GET("/api/v1/movies")
    suspend fun getMovies(): Response<List<Movie>>

    @GET("/api/v1/movies")
    suspend fun getMoviesBySearch(
        @Query("search") search: String
    ): Response<List<Movie>>
}