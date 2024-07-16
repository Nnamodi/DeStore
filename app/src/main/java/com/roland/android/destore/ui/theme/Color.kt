package com.roland.android.destore.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Brown = Color(0xFFE89705)
val Purple = Color(0xFF9747FF)
val Green = Color(0xFF09C53B)
val DarkGreen = Color(0xFF039C2B)
val SkyBlue = Color(0xFF0266B8)
val Yellow = Color(0xFFFFCD00)
val Red = Color(0xFFEC064F)
val Black = Color(0xFF141B34)
val Grey = Color(0xFFEAEAEA)


val LightColorScheme = lightColorScheme(
	primary = SkyBlue,
	secondary = SkyBlue.copy(alpha = 0.9f),
	tertiary = Brown
)

val DarkColorScheme = darkColorScheme(
	primary = SkyBlue.copy(alpha = 0.5f),
	secondary = SkyBlue.copy(alpha = 0.5f),
	tertiary = Yellow
)
