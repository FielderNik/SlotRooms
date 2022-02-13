package com.testing.slotrooms.presentation.views.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R
import com.testing.slotrooms.ui.theme.GreenMain
import com.testing.slotrooms.ui.theme.RedMain

@Composable
fun TextButtonConfirm(
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick.invoke()
    }) {
        Text(text = stringResource(id = R.string.action_confirm))
    }
}

@Composable
fun TextButtonDismiss(
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick.invoke()
    }) {
        Text(text = stringResource(id = R.string.action_dismiss))
    }
}

@Composable
fun ButtonSave(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = GreenMain),
        modifier = modifier,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.action_save),
            style = MaterialTheme.typography.button,
        )

    }
}

@Composable
fun ButtonCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = RedMain),
        modifier = modifier,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.action_cancel),
            style = MaterialTheme.typography.button,
        )

    }
}

@Preview
@Composable
fun ButtonConfirmPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

        Column() {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                ButtonCancel(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    onClick = {})
                Spacer(modifier = Modifier.padding(16.dp))
                ButtonSave(modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .shadow(20.dp), onClick = {})
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButtonConfirm {

                }
                TextButtonDismiss {

                }
            }
        }

    }

}