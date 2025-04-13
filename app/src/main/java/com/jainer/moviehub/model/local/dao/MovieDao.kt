package com.jainer.moviehub.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jainer.moviehub.model.local.Movie

// INTERFACE QUE ATUA COMO OBJETO DE ACESSO DIRETO (SGBD) DO BANCO DE DADOS DOS FILMES
@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie")
    fun getMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE title LIKE :search")
    fun getMoviesBySearch(search: String): List<Movie>

    @Insert
    fun insertMovie(movie: Movie)

    @Query("DELETE FROM Movie WHERE id=:id")
    fun deleteMovie(id: String)
}