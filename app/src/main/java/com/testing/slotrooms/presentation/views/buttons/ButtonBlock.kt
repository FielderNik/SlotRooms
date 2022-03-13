package com.testing.slotrooms.presentation.views.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R


@Composable
fun ButtonBlock(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onResetClicked: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            ButtonCancel(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                onClick = onCancelClicked
            )
            Spacer(modifier = Modifier.weight(0.1f))
            ButtonSave(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                onClick = onSaveClicked
            )
        }

        if (onResetClicked != null) {
            Spacer(modifier = Modifier.height(16.dp))

            ButtonCancel(
                text = stringResource(id = R.string.action_reset),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = onResetClicked
            )
        }
    }

}