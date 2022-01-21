package com.testing.slotrooms.presentation.views.buttons

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.testing.slotrooms.R

@Composable
fun TextButtonConfirm(
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick.invoke()
    }) {
        Text(text = stringResource(id = R.string.action_confirm))
    }
}

@Composable
fun TextButtonDismiss(
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick.invoke()
    }) {
        Text(text = stringResource(id = R.string.action_dismiss))
    }
}