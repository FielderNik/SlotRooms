package com.testing.slotrooms.presentation.views.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R

@Composable
fun ErrorDialog(
    title: String,
    message: String? = null,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit,
    onNeutralButtonClicked: (() -> Unit)? = null,
) {

    val dialogState: MutableState<Boolean> = remember {
        mutableStateOf(true)
    }

    if (dialogState.value) {
        AlertDialog(
            title = {
                Text(text = title)
            },
            text = {
                if (message != null) {
                    Text(text = message)
                }
            },
            onDismissRequest = onNegativeButtonClicked,
            confirmButton = {
                PositiveButton(
                    dialogState = dialogState,
                    onPositiveButtonClicked = onPositiveButtonClicked
                )
            },
            dismissButton = {
                NegativeButton(
                    dialogState = dialogState,
                    onNegativeButtonClicked = onNegativeButtonClicked
                )
            }
        )
    }
}

@Composable
private fun PositiveButton(
    dialogState: MutableState<Boolean>,
    onPositiveButtonClicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.action_confirm),
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                dialogState.value = false
                onPositiveButtonClicked()
            }
    )
}

@Composable
private fun NegativeButton(
    dialogState: MutableState<Boolean>,
    onNegativeButtonClicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.action_dismiss),
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                dialogState.value = false
                onNegativeButtonClicked()
            }
    )
}

@Composable
private fun NeutralButton(
    dialogState: MutableState<Boolean>,
    onNegativeButtonClicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.action_cancel),
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                dialogState.value = false
                onNegativeButtonClicked()
            }
    )
}