package com.example.shoppinglistapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = Primary,
    background = Background,
    onPrimary = White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun ShoppingListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}