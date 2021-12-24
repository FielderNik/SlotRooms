package com.testing.slotrooms.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.testing.slotrooms.R

val MainFont = FontFamily(
    Font(resId = R.font.montserrat_black, weight = FontWeight.Black, style = FontStyle.Normal),
    Font(resId = R.font.montserrat_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(resId = R.font.montserrat_regular, weight = FontWeight.Normal, style = FontStyle.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = MainFont,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
