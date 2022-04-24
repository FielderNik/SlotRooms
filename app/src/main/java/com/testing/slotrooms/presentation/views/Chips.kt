package com.testing.slotrooms.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.testing.slotrooms.R
import com.testing.slotrooms.ui.theme.IconColor
import com.testing.slotrooms.ui.theme.LocalSlotFilter
import java.text.SimpleDateFormat

@Composable
fun FilterChipDate() {
    val localSlotFilter = LocalSlotFilter.current
    val sdf = SimpleDateFormat("dd MMM")
    val startDate = sdf.format(localSlotFilter?.beginDate)
    val endDate = sdf.format(localSlotFilter?.endDate)

    Box(
        modifier = Modifier
            .background(IconColor, shape = RoundedCornerShape(50)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$startDate - $endDate",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.clickable {
                    localSlotFilter?.apply {
                        this.beginDate = null
                        this.endDate = null
                    }
                    localSlotFilter?.onFilterChanged?.invoke()
                },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FilterChipRoom() {
    val localSlotFilter = LocalSlotFilter.current
    Box(
        modifier = Modifier
            .background(IconColor, shape = RoundedCornerShape(50)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localSlotFilter?.room?.name ?: "",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.clickable {
                    localSlotFilter?.apply {
                        this.room = null
                    }
                    localSlotFilter?.onFilterChanged?.invoke()
                },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FilterGroup() {
    val localSlotFilter = LocalSlotFilter.current

    Row(modifier = Modifier.padding(top = 8.dp)) {
        if (localSlotFilter?.endDate != null && localSlotFilter.beginDate != null) {
            FilterChipDate()
            Spacer(modifier = Modifier.width(12.dp))
        }
        if (localSlotFilter?.room != null) {
            FilterChipRoom()
        }
    }
}