package com.shub39.rush.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.materialkolor.rememberDynamicColorScheme
import com.shub39.rush.domain.dataclasses.Theme
import com.shub39.rush.domain.enums.AppTheme
import com.shub39.rush.presentation.toFontRes
import com.shub39.rush.presentation.toMPaletteStyle

@Composable
fun RushTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val isDark = when (theme.appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    val fallbackScheme = rememberDynamicColorScheme(
        seedColor = Color(theme.seedColor),
        isDark = isDark,
        isAmoled = theme.withAmoled,
        style = theme.style.toMPaletteStyle(),
    )

    val dynamicScheme =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDark) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        } else {
            fallbackScheme
        }

    val colorScheme =
        if (theme.materialTheme) {
            dynamicScheme
        } else {
            fallbackScheme
        }

    val finalScheme =
        if (theme.withAmoled && isDark) {
            colorScheme.copy(
                background = Color.Black,
                surface = Color.Black,
            )
        } else {
            colorScheme
        }

    MaterialExpressiveTheme(
        colorScheme = finalScheme,
        motionScheme = MotionScheme.expressive(),
        typography = provideTypography(theme.font.toFontRes()),
        content = content,
    )
}