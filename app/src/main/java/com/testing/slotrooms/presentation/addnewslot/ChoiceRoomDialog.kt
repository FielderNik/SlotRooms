package com.testing.slotrooms.presentation.addnewslot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testing.slotrooms.R
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Users
import com.testing.slotrooms.presentation.views.buttons.TextButtonDismiss

@Composable
fun ChoiceRoomDialog(
    viewModel: ChoiceRoomViewModel = viewModel(),
    onConfirmClicked: (responseData: String) -> Unit,
    onDismiss: () -> Unit,
    dialogType: DialogType
) {
    val dialogState = remember {
        mutableStateOf(true)
    }
    val dataList = if (dialogType == DialogType.ROOM) {
        viewModel.rooms.collectAsState()
    } else {
        viewModel.owners.collectAsState()
    }
//    val dataList = viewModel.rooms.collectAsState()
    val titleDialog = if (dialogType == DialogType.ROOM) {
        stringResource(id = R.string.title_dialog_choice_room)
    } else {
        stringResource(id = R.string.title_dialog_owners)
    }
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
                onDismiss.invoke()
            },
            title = {
                Text(
                    text = titleDialog,
                    style = MaterialTheme.typography.h3,
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(dataList.value) { index, data ->
                        when(data) {
                            is Rooms -> {
                                SlotContentView(
                                    needDivider = index < dataList.value.lastIndex,
                                    room = data.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onConfirmClicked.invoke(data.name)
                                            dialogState.value = false
                                        }
                                )
                            }
                            is Users -> {
                                SlotContentView(
                                    needDivider = index < dataList.value.lastIndex,
                                    room = data.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onConfirmClicked.invoke(data.name)
                                            dialogState.value = false
                                        }
                                )
                            }
                        }

                    }
                }
            },
            confirmButton = {
//                Text(text = "Confirm", modifier = Modifier
//                    .padding(4.dp)
//                    .clickable {
//                        onConfirmClicked.invoke("room")
//                    })

            },
            dismissButton = {
                Text(
                    text = stringResource(id = R.string.action_dismiss),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            dialogState.value = false
                            onDismiss.invoke()
                        })
            },
        )
    }
}

@Composable
fun SlotContentView(needDivider: Boolean, room: String, modifier: Modifier) {
    Box(modifier = modifier) {
        Text(
            text = room,
            style = MaterialTheme.typography.body2,
        )
    }
    if (needDivider) {
        Divider(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun ChoiceRoomDialog2(
    viewModel: ChoiceRoomViewModel = viewModel(),
    onConfirmClicked: (room: String) -> Unit,
    onDismiss: () -> Unit,
    dialogState: MutableState<SlotDialog>
) {
    if (dialogState.value.isOpen) {
        val dataForDialog = if (dialogState.value.dialogType == DialogType.ROOM) viewModel.defaultRooms else viewModel.defaultOwners
        val titleDialog = if (dialogState.value.dialogType == DialogType.ROOM) {
            stringResource(id = R.string.title_dialog_choice_room)
        } else {
            stringResource(id = R.string.title_dialog_owners)
        }
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
            },
            title = {
                Text(
                    text = titleDialog,
                    style = MaterialTheme.typography.h3,
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(dataForDialog) { index, data ->
                        SlotContentView(
                            needDivider = index < dataForDialog.lastIndex,
                            room = data,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onConfirmClicked.invoke(data)
                                }
                        )
                    }
                }
            },
            confirmButton = {
//                Text(text = "Confirm", modifier = Modifier
//                    .padding(4.dp)
//                    .clickable {
//                        onConfirmClicked.invoke("room")
//                    })

            },
            dismissButton = {
                TextButtonDismiss(onClick = onDismiss)
/*                Text(
                    text = stringResource(id = R.string.action_dismiss),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            onDismiss.invoke()
                        })*/
            },
        )
    }
}


@Preview
@Composable
fun ChoiceRoomDialog_Preview() {
    ChoiceRoomDialog(
        viewModel = viewModel<ChoiceRoomViewModel>(),
        onConfirmClicked = { },
        onDismiss = {},
        dialogType = DialogType.ROOM
    )
}