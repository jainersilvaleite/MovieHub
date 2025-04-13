package com.jainer.moviehub.model.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jainer.moviehub.MainApplication
import com.jainer.moviehub.model.local.Movie
import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.remote.User
import com.jainer.moviehub.model.remote.api.RetrofitInstance
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType

// REPOSITÓRIO QUE CONTÉM OS MÉTODOS PARA MANUSEAR OS FILMES DO APLICATIVO
class MovieRepository {
    // referenciando a API Fake de filmes
    private val movieApi = RetrofitInstance.movieApi
    // referenciando o objeto de acesso direto ao banco de dados de filmes salvos offline
    private val _movieDao = MainApplication.movieDatabase.getMovieDao()

    suspend fun getMovies(
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            val response = movieApi.getMovies() // <-- coleta o resultado da operação
            // RETORNA A LISTA DE FILMES DA API FAKE EM CASO DE SUCESSO
            if (response.isSuccessful) {
                response.body()?.let { movies ->
                    onAppSuccess(
                        AppSuccess(
                            successType = AppSuccessType.FAKE_MOVIES_API_SUCCESS,
                            message = "Filmes carregados com sucesso!",
                            movieApiResult = movies
                        )
                    )
                    return
                }
            }

            // RETORNO DE ERRO ESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.FAKE_MOVIES_API_ERROR,
                    message = response.message(),
                    appMessage = "Ocorreu um erro ao carregar os filmes!"
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.FAKE_MOVIES_API_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao carregar os filmes!"
                )
            )
        }
    }

    suspend fun getMoviesBySearch(
        search: String,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            val response = movieApi.getMoviesBySearch(search) // <-- coleta o resultado da operação
            // RETORNA A LISTA DE FILMES DA API FAKE CORRESPONDENTES A BUSCA EM CASO DE SUCESSO
            if (response.isSuccessful) {
                response.body()?.let { movies ->
                    onAppSuccess(
                        AppSuccess(
                            successType = AppSuccessType.FAKE_MOVIES_API_SUCCESS,
                            message = "Busca realizada com sucesso!",
                            movieApiResult = movies
                        )
                    )
                    return
                }
            }

            // RETORNO DE ERRO ESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.FAKE_MOVIES_API_ERROR,
                    message = response.message(),
                    appMessage = "Ocorreu um erro ao realizar a busca!"
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.FAKE_MOVIES_API_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao realizar a busca!"
                )
            )
        }
    }

    fun insertMovie(
        movie: Movie,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            _movieDao.insertMovie(movie)

            onAppSuccess(
                AppSuccess(
                    successType = AppSuccessType.MOVIE_LOCAL_DB_SUCCESS,
                    message = "Filme salvo com sucesso, você pode consultá-lo mesmo offline!"
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.MOVIE_LOCAL_DB_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao salvar offline!"
                )
            )
        }
    }

    fun deleteMovie(
        movieId: String,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            _movieDao.deleteMovie(movieId)

            onAppSuccess(
                AppSuccess(
                    successType = AppSuccessType.MOVIE_LOCAL_DB_SUCCESS,
                    message = "O filme foi removido da sua lista de salvamentos!"
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.MOVIE_LOCAL_DB_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao remover este filme!"
                )
            )
        }
    }

    fun getSavedMovies(
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            val movieLocalDbResult = _movieDao.getMovies()

            onAppSuccess(
                AppSuccess(
                    successType = AppSuccessType.MOVIE_LOCAL_DB_SUCCESS,
                    message = "Os filmes salvos foram carregados com sucesso!",
                    movieDbResult = movieLocalDbResult
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.MOVIE_LOCAL_DB_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao carregar os filmes salvos!"
                )
            )
        }
    }

    fun getSavedMoviesBySearch(
        search: String,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            val movieLocalDbResult = _movieDao.getMoviesBySearch("%$search%")

            onAppSuccess(
                AppSuccess(
                    successType = AppSuccessType.MOVIE_LOCAL_DB_SUCCESS,
                    message = "Busca realizada com sucesso!",
                    movieDbResult = movieLocalDbResult
                )
            )
        } catch (e: Exception) {
            // RETORNO DE ERRO INESPERADO PELO APLICATIVO
            onAppError(
                AppError(
                    errorType = AppErrorType.MOVIE_LOCAL_DB_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao realizar a busca!"
                )
            )
        }
    }

    fun getComments(
        movieId: String,
        databaseReference: DatabaseReference,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        databaseReference.child("comments").child(movieId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var comments = emptyList<Comment>()
                        snapshot.children.forEach {
                            comments = comments + (it.getValue(Comment::class.java))!!
                        }

                        onAppSuccess(
                            AppSuccess(
                                successType = AppSuccessType.FIREBASE_DB_SUCCESS,
                                message = "Os comentários do filme foram carregados com sucesso!",
                                commentsResult = comments
                            )
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onAppError(
                            AppError(
                                errorType = AppErrorType.FIREBASE_DB_ERROR,
                                cause = error.toException().cause,
                                message = error.toException().message,
                                appMessage = "Ocorreu um erro ao carregar os comentários deste filme!"
                            )
                        )
                    }

                }
            )
    }

    fun getCommentsQuantity(
        movieId: String,
        databaseReference: DatabaseReference,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        databaseReference.child("comments").child(movieId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        onAppSuccess(
                            AppSuccess(
                                successType = AppSuccessType.FIREBASE_DB_SUCCESS,
                                message = "Quantidade de comentários obtida com sucesso!",
                                commentsQttResult = snapshot.childrenCount
                            )
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onAppError(
                            AppError(
                                errorType = AppErrorType.FIREBASE_DB_ERROR,
                                cause = error.toException().cause,
                                message = error.toException().message,
                                appMessage = "Ocorreu um erro ao calcular a quantidade de comentários!"
                            )
                        )
                    }

                }
            )
    }
}