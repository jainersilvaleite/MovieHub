package com.jainer.moviehub.model.repository

import com.google.firebase.database.DatabaseReference
import com.jainer.moviehub.model.remote.Comment
import com.jainer.moviehub.model.remote.User
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType

// REPOSITÓRIO QUE CONTÉM OS MÉTODOS PARA MANUSEAR OS USUÁRIOS DO APLICATIVO
class UserRepository {
    fun getUser(
        userId: String,
        databaseReference: DatabaseReference,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        databaseReference.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)

                // verifica se o usuário foi encontrado para então retorná-lo
                if (user != null) {
                    onAppSuccess(
                        AppSuccess(
                            successType = AppSuccessType.FIREBASE_DB_SUCCESS,
                            message = "Usuário coletado com sucesso!",
                            userResult = user
                        )
                    )
                } else {
                    onAppError(
                        AppError(
                            errorType = AppErrorType.FIREBASE_DB_ERROR,
                            appMessage = "Usuário não encontrado!"
                        )
                    )
                }
            }
            .addOnFailureListener { failure ->
                onAppError(
                    AppError(
                        errorType = AppErrorType.FIREBASE_DB_ERROR,
                        cause = failure.cause, message = failure.message,
                        appMessage = "Ocorreu um erro ao coletar o usuário especificado!"
                    )
                )
            }
    }

    fun sendComment(
        movieId: String,
        comment: Comment,
        databaseReference: DatabaseReference,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        try {
            // cria um id para o comentário a ser adicionado
            val commentId = databaseReference
                .child("comments")
                .child(movieId)
                .push().key!!

            with(comment) {
                // mapa com os dados a serem armazenados do comentário
                val commentMap = mapOf(
                    "content" to content,
                    "createdAt" to createdAt,
                    "authorId" to authorId,
                    "authorName" to authorName
                )

                // criação de um espaço para armazenamento do comentário no realtime database
                databaseReference
                    .child("comments")
                    .child(movieId)
                    .child(commentId)
                    .setValue(commentMap)
            }

            onAppSuccess(
                AppSuccess(
                    successType = AppSuccessType.FIREBASE_DB_SUCCESS,
                    message = "Comentário adicionado com sucesso!"
                )
            )
        } catch (e: Exception) {
            onAppError(
                AppError(
                    errorType = AppErrorType.FIREBASE_DB_ERROR,
                    cause = e.cause, message = e.message,
                    appMessage = "Ocorreu um erro ao adicionar o comentário!"
                )
            )
        }
    }
}