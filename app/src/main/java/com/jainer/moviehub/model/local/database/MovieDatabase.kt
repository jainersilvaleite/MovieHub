package com.jainer.moviehub.model.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jainer.moviehub.model.local.Movie
import com.jainer.moviehub.model.local.converters.MovieConverters
import com.jainer.moviehub.model.local.dao.MovieDao

// BANCO DE DADOS LOCAL QUE ARMAZENARÁ OS FILMES SALVOS OFFLINE
@Database(entities = [Movie::class], version = 1)
@TypeConverters(MovieConverters::class)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}