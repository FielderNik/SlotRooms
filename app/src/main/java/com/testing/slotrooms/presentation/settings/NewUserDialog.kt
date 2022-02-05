package com.testing.slotrooms.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.views.buttons.TextButtonConfirm
import com.testing.slotrooms.presentation.views.buttons.TextButtonDismiss

@Composable
fun NewUserDialog(viewModel: SettingsViewModel) {
    var userName by remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {
            viewModel.handleEvent(SettingsScreenEvent.OpenDefaultScreen)
        },
        title = {
            Text(
                text = stringResource(id = R.string.title_new_user),
                style = MaterialTheme.typography.h3,
            )
        },
        text = {
            Column {
                BasicTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Divider(modifier = Modifier.padding(top = 2.dp))
            }
        },
        confirmButton = {
            TextButtonConfirm(
                onClick = {
                    viewModel.handleEvent(SettingsScreenEvent.NewUser.NewUserConfirmed(userName = userName))
                }
            )

        },
        dismissButton = {
            TextButtonDismiss(
                onClick = {
                    viewModel.handleEvent(SettingsScreenEvent.OpenDefaultScreen)
                }
            )
        },
    )
}