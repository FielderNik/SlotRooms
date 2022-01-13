package com.testing.slotrooms.presentation.addnewslot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R

@Composable
fun AddNewSlotScreen(
    isNewSlot: Boolean
) {
    Scaffold() {
        Column {
            ToolbarAddNewSlot(isNewSlot)
            RoomEditBlock()

        }
    }
}

@Composable
fun ToolbarAddNewSlot(
    isNewSlot: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = null
        )
        Text(
            text =
            if (isNewSlot) {
                stringResource(id = R.string.title_new_slot)
            } else {
                stringResource(id = R.string.title_edit_slot)
            },
            style = MaterialTheme.typography.h1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoomEditBlock() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "Room",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(text = "Choice room",
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {

                    ChoiceRoomDialog()
                }
        )
        Divider()
    }
}

@Preview
@Composable
fun AddNewSlotScreen_Preview() {
    AddNewSlotScreen(isNewSlot = true)
}