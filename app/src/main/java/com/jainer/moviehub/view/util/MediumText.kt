package com.jainer.moviehub.view.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

// TEXTO QUE REPRESENTA INSTRUÇÕES DE TAMANHO MÉDIO PARA O APLICATIVO
@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = Color.Black,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        textAlign = textAlign,
        color = color,
        fontWeight = fontWeight,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}