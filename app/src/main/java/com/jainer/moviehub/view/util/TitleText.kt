package com.jainer.moviehub.view.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

// TEXTO QUE REPRESENTA UM TÍTULO PARA O APLICATIVO
@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = Color.Black,
    fontWeight: FontWeight? = FontWeight.Bold
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        textAlign = textAlign,
        color = color,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}