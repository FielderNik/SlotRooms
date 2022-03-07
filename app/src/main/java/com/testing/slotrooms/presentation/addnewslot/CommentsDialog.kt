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
import androidx.hilt.navigation.compose.hiltViewModel
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.views.buttons.TextButtonConfirm
import com.testing.slotrooms.presentation.views.buttons.TextButtonDismiss

@Composable
fun CommentDialog(
    commentDialogState: MutableState<Boolean>,
    viewModel: AddNewSlotViewModel
) {

    var comment by remember {
        mutableStateOf("")
    }

    if (commentDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                commentDialogState.value = false
            },
            confirmButton = {

                TextButtonConfirm {
                    commentDialogState.value = false
                    viewModel.updateComment(comment)
                }
            },
            dismissButton = {
                TextButtonDismiss {
                    commentDialogState.value = false
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
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
        commentDialogState = remember {
            mutableStateOf(true)
        },
        viewModel = hiltViewModel()
    )
}