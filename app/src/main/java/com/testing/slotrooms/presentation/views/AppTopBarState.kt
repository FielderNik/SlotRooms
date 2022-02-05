package com.testing.slotrooms.presentation.views

import androidx.compose.Composable

data class AppTopBarState(
    val title: String = "Slots",
    val navigateIcon: @Composable (() -> Unit)? = null,
    val optionIcon: @Composable (() -> Unit)? = null,
    val isShowBack: Boolean = false,
    val isShowFilter: Boolean = false

)