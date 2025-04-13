package com.jainer.moviehub.model.repository

import android.content.Context
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jainer.moviehub.AuthActivity
import com.jainer.moviehub.model.util.AppError
import com.jainer.moviehub.model.util.AppErrorType
import com.jainer.moviehub.model.util.AppSuccess
import com.jainer.moviehub.model.util.AppSuccessType
import com.jainer.moviehub.model.util.AuthCredential
import com.jainer.moviehub.view.navigation.AuthRoutes

// REPOSITÓRIO QUE CONTÉM OS MÉTODOS PARA AUTENTICAÇÃO DE USUÁRIOS
class AuthRepository {
    fun login(
        authCredential: AuthCredential,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        // SEGUE COM O LOGIN SE AS CREDENCIAIS ATENDEREM AOS CRITÉRIOS
        with(authCredential) {
            if (isLoginCredentialValid(authCredential, onAppSuccess, onAppError)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        with(task) {
                            if (isSuccessful) {
                                onAppSuccess(
                                    AppSuccess(
                                        successType = AppSuccessType.LOGIN_SUCCESS,
                                        message = "Login realizado com sucesso!"
                                    )
                                )
                            } else {
                                onAppError(
                                    AppError(
                                        errorType = AppErrorType.AUTH_ERROR,
                                        message = exception?.message,
                                        cause = exception?.cause,
                                        appMessage = "Ocorreu um erro ao fazer login!"
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    fun signup(
        authCredential: AuthCredential,
        navController: NavController,
        onAppSuccess: (AppSuccess) -> Unit,
        onAppError: (AppError) -> Unit
    ) {
        // SEGUE COM O CADASTRO SE AS CREDENCIAIS ATENDEREM AOS CRITÉRIOS
        with(authCredential) {
            if (isSignupCredentialValid(authCredential, onAppSuccess, onAppError)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        with(task) {
                            if (isSuccessful) {
                                /* ARMAZENAMENTO DOS DADOS DO USUÁRIO NO REALTIME DATABASE
                                * (!) Ao cadastrar o usuário, é feito um login temporário para
                                * coletar suas informações e salvar no Realtime Database dentro
                                * de um espaço reservado para seu id */
                                // mapa com os dados a serem armazenados do usuário
                                val userMap = mapOf("username" to username, "createdAt" to createdAt)

                                // cria um espaço para armazenamento no Realtime Database
                                auth.uid?.let { uid ->
                                    databaseReference
                                        ?.child("users")
                                        ?.child(uid)
                                        ?.setValue(userMap)
                                }

                                // encerra o login temporário do usuário recém-cadastrado
                                auth.signOut()

                                // MENSAGEM DE SUCESSO
                                onAppSuccess(
                                    AppSuccess(
                                        successType = AppSuccessType.SIGNUP_SUCCESS,
                                        message = "Cadastro realizado com sucesso!"
                                    )
                                )

                                // REDIRECIONAMENTO PARA LOGINSCREEN
                                navController.navigate(AuthRoutes.loginScreen)
                            } else {
                                onAppError(
                                    AppError(
                                        errorType = AppErrorType.AUTH_ERROR,
                                        message = exception?.message,
                                        cause = exception?.cause,
                                        appMessage = "Ocorreu um erro ao fazer cadastro!"
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    fun logout(
        auth: FirebaseAuth,
        context: Context
    ) {
        auth.signOut()
        navigateToActivity(AuthActivity::class.java, context)
    }
}