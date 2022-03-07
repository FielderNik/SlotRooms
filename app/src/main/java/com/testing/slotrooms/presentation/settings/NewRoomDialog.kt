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
fun NewRoomDialog(
    viewModel: SettingsViewModel,
    dialogState: MutableState<Boolean>
) {
    var roomName by remember {
        mutableStateOf("")
    }

    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
//                viewModel.handleEvent(SettingsScreenEvent.OpenDefaultScreen)
            },
            title = {
                Text(
                    text = stringResource(id = R.string.title_new_room),
                    style = MaterialTheme.typography.h3,
                )
            },
            text = {
                Column {
                    BasicTextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        textStyle = MaterialTheme.typography.body1,
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
                        dialogState.value = false
                        viewModel.saveNewRoom(roomName)
//                        viewModel.handleEvent(SettingsScreenEvent.NewRoom.NewRoomConfirmed(roomName = roomName))
                    }
                )

            },
            dismissButton = {
                TextButtonDismiss(
                    onClick = {
                        dialogState.value = false
//                        viewModel.handleEvent(SettingsScreenEvent.OpenDefaultScreen)
                    }
                )
            },
        )
    }

}