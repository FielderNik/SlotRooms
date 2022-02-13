package com.testing.slotrooms.presentation.views.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ButtonBlock(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
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
}