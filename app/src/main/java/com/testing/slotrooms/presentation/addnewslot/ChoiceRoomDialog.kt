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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testing.slotrooms.R
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users

@Composable
fun <T> ChoiceRoomDialog(
    viewModel: AddNewSlotViewModel,
    dialogType: DialogType,
    dataList: List<T>,
) {
    val dialogState = remember {
        mutableStateOf(true)
    }

    val titleDialog = if (dialogType == DialogType.ROOM) {
        stringResource(id = R.string.title_dialog_choice_room)
    } else {
        stringResource(id = R.string.title_dialog_owners)
    }
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
                viewModel.updateDisplayState()
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
                    itemsIndexed(dataList) { index, data ->
                        when (data) {
                            is Rooms -> {
                                SlotContentView(
                                    needDivider = index < dataList.lastIndex,
                                    name = data.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.updateRoom(data as Rooms)
                                            dialogState.value = false
                                        }
                                )
                            }
                            is Users -> {
                                SlotContentView(
                                    needDivider = index < dataList.lastIndex,
                                    name = data.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.updateUser(data as Users)
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
                            viewModel.updateDisplayState()
                        })
            },
        )
    }
}

@Composable
fun SlotContentView(needDivider: Boolean, name: String, modifier: Modifier) {
    Box(modifier = modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.body2,
        )
    }
    if (needDivider) {
        Divider(modifier = Modifier.padding(top = 4.dp))
    }
}


@Preview
@Composable
fun ChoiceRoomDialog_Preview() {
    ChoiceRoomDialog<Rooms>(
        viewModel = viewModel<AddNewSlotViewModel>(),
        dialogType = DialogType.ROOM,
        dataList = listOf()

    )
}