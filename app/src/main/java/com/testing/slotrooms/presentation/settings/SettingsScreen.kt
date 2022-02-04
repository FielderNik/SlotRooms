package com.testing.slotrooms.presentation.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.testing.slotrooms.presentation.views.AppTopBarState


@Composable
fun SettingsScreen(appTopBarState: MutableState<AppTopBarState>) {
    LaunchedEffect(Unit) {
        appTopBarState.value = appTopBarState.value.copy(title = "SETTINGS")
    }
    Text(text = "Settings")
}