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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testing.slotrooms.R


@Composable
fun ChoiceRoomDialog(
    viewModel: AddNewSlotViewModel = viewModel<AddNewSlotViewModelImpl>(),
    onConfirmClicked: (room: String) -> Unit,
    onDismiss: () -> Unit,
    dialogState: MutableState<Boolean>
) {
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
            },
            title = {
                Text(
                    text = stringResource(id = R.string.title_dialog_choice_room),
                    style = MaterialTheme.typography.h3,
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(viewModel.rooms) { index, room ->
                        SlotContentView(
                            needDivider = index < viewModel.rooms.lastIndex,
                            room = room,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onConfirmClicked.invoke(room)
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
                Text(
                    text = stringResource(id = R.string.action_dismiss),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
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

@Preview
@Composable
fun ChoiceRoomDialog_Preview() {
    val dialogState = remember {
        mutableStateOf(false)
    }
    ChoiceRoomDialog(
        viewModel = AddNewSlotViewModelImplPreview(listOf("23")),
        onConfirmClicked = { },
        onDismiss = {},
        dialogState = dialogState
    )
}