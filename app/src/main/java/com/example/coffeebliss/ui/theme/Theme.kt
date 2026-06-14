package com.example.coffeebliss.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoffeeColorScheme = lightColorScheme(
    primary              = EspressoDark,
    onPrimary            = Color(0xFFFFF8F0),
    primaryContainer     = EspressoMedium,
    onPrimaryContainer   = MochaText,
    secondary            = GoldCoffee,
    onSecondary          = EspressoDark,
    secondaryContainer   = GoldContainer,
    onSecondaryContainer = EspressoDark,
    tertiary             = GoldBorder,
    onTertiary           = EspressoDark,
    background           = MochaCream,
    onBackground         = MochaDark,
    surface              = Color.White,
    onSurface            = MochaDark,
    surfaceVariant       = MochaContainer,
    onSurfaceVariant     = MochaLabel,
    outline              = MochaBrown,
    outlineVariant       = MochaDivider,
    error                = ErrorRed,
    onError              = Color.White,
)

@Composable
fun CoffeeBlissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography  = Typography,
        content     = content
    )
}
