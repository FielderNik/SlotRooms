package com.testing.slotrooms.presentation.views.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.testing.slotrooms.R
import com.testing.slotrooms.ui.theme.IconBackground
import com.testing.slotrooms.ui.theme.IconColor

@Composable
fun FilterIcon(
    modifier: Modifier
) {
    Box(modifier = modifier
        .background(color = IconBackground, shape = RoundedCornerShape(8.dp))
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "",
            tint = IconColor
        )
    }
}