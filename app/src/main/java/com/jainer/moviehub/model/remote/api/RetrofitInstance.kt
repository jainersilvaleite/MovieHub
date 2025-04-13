package com.jainer.moviehub.model.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/* TODAS AS INSTÂNCIAS DO RETROFIT UTILIZADAS NO APLICATIVO
* (!) Cada instância realiza a conexão com uma API diferente */
object RetrofitInstance {
    // url base da API Fake de filmes
    private const val fakeMoviesApiBaseUrl = "https://freetestapi.com"

    // instância Retrofit para conexão com a API Fake de filmes
    private fun getFakeMoviesApiInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(fakeMoviesApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // associa a instância Retrofit da API Fake de filmes com suas operações
    val movieApi = getFakeMoviesApiInstance().create(MovieApi::class.java)
}