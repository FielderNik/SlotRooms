package com.testing.slotrooms.ui.theme

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val YellowMain = Color(0xFFFFD481)
val GreenMain = Color(0xFFA0E189)
val GreyFont = Color(0xFF727272)
//val MainBackground = Color(0xFFFFF7E3)
val MainBackground = Color(0xFFFDFAF2)
val MainBackgroundGrey = Color(0xFFFAFAFA)
val MainBackgroundGrey2 = Color(0xFFF5F5F5)
val RedMain = Color(0xFFE6908C)
val MainSurface = Color(0xFF8F7944)
val GreenDark = Color(0xFF175F23)
val MainBlue = Color(0xFF396EF8)

val GreyIcon = Color(0xFF848484)

val FabBackground = Color(0xFF32C064)
val PositiveDateChip = Color(0xFF32C064)
val NegativeDateChip = Color(0xFFE1980B)
val RoomChipBackground = Color(0xFFEAF0FF)
val RoomNameTextColor = Color(0xFF396EF8)

val DividerColor = Color(0xB4DADEFF)

val MainBlueBackground = Color(0xFFEFF1FF)

val IconBackground = Color(0xFFD9EDFF)
val IconColor = Color(0xFF396EF8)

val DeleteIconColor = Color(0xFF9B0505)

//val ButtonBackground = Color(0xFFD9EDFF)
val ButtonBackground = Color(0xFF396EF8)

// outlinedTextField
@Composable
fun appOutlinedTextFieldColors() : TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(

        focusedBorderColor = MainBlue,
        unfocusedBorderColor = IconBackground,
        placeholderColor = GreyFont.copy(alpha = 0.8f)
    )
}

