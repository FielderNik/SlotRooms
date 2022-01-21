package com.testing.slotrooms.presentation.addnewslot

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.testing.slotrooms.presentation.views.buttons.TextButtonConfirm

import com.testing.slotrooms.presentation.views.buttons.TextButtonDismiss

@Composable
fun CommentDialog(
    onConfirmClicked: (comment: String) -> Unit,
    onDismissClicked: () -> Unit
) {
    val dialogState = remember {
        mutableStateOf(true)
    }
    var comment by remember {
        mutableStateOf("")
    }

    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
            },
            confirmButton = {
                TextButtonConfirm {
                    onConfirmClicked.invoke(comment)
                }
            },
            dismissButton = {
                TextButtonDismiss {
                    dialogState.value = false
                    onDismissClicked.invoke()
                }
            },
            title = {
                Text(text = "Comment")
            },
            text = {
                TextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )

    }
}

@Composable
fun CommentEditField(comment: MutableState<String>) {
    var comment by remember {
        mutableStateOf("")
    }
    TextField(
        value = comment,
        onValueChange = { comment = it },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showSystemUi = true)
@Composable
fun CommentDialogPreview() {
    CommentDialog(
        onConfirmClicked = {},
        onDismissClicked = {}
    )
}