package com.testing.slotrooms.presentation.addnewslot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ChoiceRoomDialog() {
    var showDialog = remember { mutableStateOf(false) }
    val viewModel: AddNewSlotViewModel = viewModel()

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = { "Choice Room" },
            text = {
                   LazyColumn() {
                       items(viewModel.rooms) { room ->
                           Text(text = room, modifier = Modifier.padding(4.dp))
                       }
                   }
            },
            confirmButton = {
                Text(text = "Confirm", modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        showDialog.value = false
                    })

            },
            dismissButton = {
                Text(text = "Dismiss", modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        showDialog.value = false
                    })

            },
        )
    }
}