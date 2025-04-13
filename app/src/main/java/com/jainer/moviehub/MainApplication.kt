package com.jainer.moviehub

import android.app.Application
import androidx.room.Room
import com.jainer.moviehub.model.local.database.MovieDatabase

// CONTEXTO DO APLICATIVO ATUAL PARA CRIAÇÃO DOS BANCOS DE DADOS
class MainApplication: Application() {
    // BANCO DE DADOS DO APLICATIVO
    companion object {
        lateinit var movieDatabase: MovieDatabase
    }

    override fun onCreate() {
        super.onCreate()
        // CONSTRUTOR DO BANCO DE DADOS DE FILMES SALVOS OFFLINE
        movieDatabase = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "MovieDB"
        ).build()
    }
}