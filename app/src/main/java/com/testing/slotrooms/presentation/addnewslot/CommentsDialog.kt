package com.testing.slotrooms.presentation.addnewslot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.presentation.views.buttons.TextButtonConfirm
import com.testing.slotrooms.presentation.views.buttons.TextButtonDismiss
import com.testing.slotrooms.R

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
                Text(text = stringResource(id = R.string.title_comment))
            },
            text = {
                Column {
                    BasicTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Divider(modifier = Modifier.padding(top = 2.dp))
                }

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