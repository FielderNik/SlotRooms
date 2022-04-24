package com.testing.slotrooms.presentation.slots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.ui.theme.GreyFont
import com.testing.slotrooms.ui.theme.GreyIcon
import com.testing.slotrooms.ui.theme.IconBackground
import com.testing.slotrooms.ui.theme.IconColor
import java.text.SimpleDateFormat

@Composable
fun SlotCardItemNew(
    slot: SlotRoom,
    navController: NavController,
) {
    val timeFormat = SimpleDateFormat("HH.mm")
    val timeTextStart = timeFormat.format(slot.beginDateTime)
    val timeTextEnd = timeFormat.format(slot.endDateTime)
    val dateMeeting = SimpleDateFormat("dd MMM").format(slot.endDateTime)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                navController.navigate(Screens.AddNewSlotScreen.passSlotRoomId(slotRoomId = slot.id))
            }
    ) {
        val (imageOffice, roomName, dateChip, timeBlock, capacityBlock) = createRefs()

        Image(
            modifier = Modifier
                .height(104.dp)
                .width(120.dp)
                .constrainAs(imageOffice) {
                    start.linkTo(parent.start, margin = 16.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .clip(RoundedCornerShape(4.dp)),
            painter = painterResource(id = R.drawable.image_office),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.8f,
        )

        Text(
            modifier = Modifier
                .constrainAs(roomName) {
                    start.linkTo(imageOffice.end, margin = 16.dp)
                    top.linkTo(parent.top, margin = 12.dp)
                },
            text = slot.room.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        DateChip(
            modifier = Modifier
                .constrainAs(dateChip) {
                    linkTo(roomName.end, parent.end, startMargin = 8.dp, endMargin = 16.dp, bias = 1f)
                    top.linkTo(parent.top, margin = 16.dp)
                    width = Dimension.preferredWrapContent
                },

            date = dateMeeting
        )

        TimeBlock(
            modifier = Modifier
                .constrainAs(timeBlock) {
                    start.linkTo(imageOffice.end, margin = 16.dp)
                    top.linkTo(roomName.bottom, margin = 16.dp)
                },
            timeStart = timeTextStart,
            timeEnd = timeTextEnd
        )

        RoomCapacity(
            modifier = Modifier
                .constrainAs(capacityBlock) {
                    start.linkTo(imageOffice.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                },
            capacity = slot.room.capacity
        )
    }
}

@Composable
private fun DateChip(
    modifier: Modifier,
    date: String
) {
    Row(
        modifier = Modifier
                then (modifier)
            .background(color = IconBackground, shape = RoundedCornerShape(4.dp))
            .padding(vertical = 4.dp, horizontal = 6.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = "")
        Text(
            text = date,
            color = IconColor,
            fontSize = 12.sp
        )
    }

}

@Composable
private fun TimeBlock(modifier: Modifier = Modifier, timeStart: String, timeEnd: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timeStart,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = GreyFont
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_time),
            contentDescription = null,
            tint = GreyIcon
        )
        Text(
            text = timeEnd,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = GreyFont
        )

    }
}

@Composable
private fun RoomCapacity(
    modifier: Modifier,
    capacity: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(painter = painterResource(id = R.drawable.ic_room_capacity), contentDescription = "")
        Text(
            text = "$capacity человек",
            color = GreyFont,
            fontSize = 12.sp
        )
    }

}



