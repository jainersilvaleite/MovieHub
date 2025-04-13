package com.jainer.moviehub.view.util

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/* CAMPO DE TEXTO ADAPTADO PARA PREENCHIMENTO DAS CREDENCIAIS DE USUÁRIO:
* (!) Utilizado para as credenciais: nome, e-mail e senha
* (!) É possível limitar o número de caracteres com o parâmetro maxLength
* (!) É possível definir um placeholder com o parâmetro placeholder */
@Composable
fun CredentialTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    maxLength: Int? = null
) {
    TextField(
        value = value,
        onValueChange = {
            // LÓGICA RESPONSÁVEL PELO CONTROLE DO MÁXIMO DE CARACTERES DO CAMPO DE TEXTO
            val textFieldValue = it
            maxLength?.let { if (textFieldValue.length <= maxLength) onValueChange(textFieldValue) }
        },
        placeholder = placeholder,
        singleLine = true,
        modifier = modifier
    )
}