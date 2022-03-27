package com.testing.slotrooms.presentation.slots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.testing.slotrooms.R
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.ui.theme.*
import java.text.SimpleDateFormat

@Composable
fun SlotCardItem(
    viewModel: SlotsViewModel,
    slot: SlotRoom,
    navController: NavController,
    filter: SlotFilter?
) {
    val timeFormat = SimpleDateFormat("HH.mm")
    val timeTextStart = timeFormat.format(slot.beginDateTime)
    val timeTextFinish = timeFormat.format(slot.endDateTime)
    val dateMeeting = SimpleDateFormat("dd MMMM").format(slot.endDateTime)
    val isEmptySlot = slot.owner.name.isEmpty()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screens.AddNewSlotScreen.passSlotRoomId(slotRoomId = slot.id))
            }

    ) {

        val (dateChip, roomChip, timeBlock, comment, avatar, divider) = createRefs()
        val bottomBarrier = createBottomBarrier(comment, avatar)
        val startGuideline = createGuidelineFromStart(16.dp)
        val topGuideline = createGuidelineFromTop(16.dp)
        val endGuideline = createGuidelineFromEnd(16.dp)
        val endCommentGuideline = createGuidelineFromEnd(0.3f)

        DateChip(
            modifier = Modifier.constrainAs(dateChip) {
                top.linkTo(topGuideline)
                start.linkTo(startGuideline)
            },
            dateSlot = dateMeeting,
            isEmptySlot = isEmptySlot
        )

        TimeBlock(
            modifier = Modifier.constrainAs(timeBlock) {
                top.linkTo(dateChip.bottom, margin = 8.dp)
                start.linkTo(startGuideline)
            },
            timeStart = timeTextStart,
            timeEnd = timeTextFinish
        )

        Text(
            modifier = Modifier.constrainAs(comment) {
                top.linkTo(timeBlock.bottom, margin = 8.dp)
                linkTo(startGuideline, endCommentGuideline, bias = 0f)
                width = Dimension.preferredWrapContent

            },
            text = slot.comments,
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        RoomChip(
            modifier = Modifier.constrainAs(roomChip) {
                top.linkTo(topGuideline)
                end.linkTo(endGuideline)
            },
            roomName = slot.room.name
        )

        AvatarBlock(
            modifier = Modifier.constrainAs(avatar) {
                top.linkTo(roomChip.bottom, margin = 12.dp)
                end.linkTo(endGuideline)
            },
            owner = slot.owner
        )

        Divider(
            modifier = Modifier
                .constrainAs(divider) {
                    top.linkTo(bottomBarrier, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            color = DividerColor
        )

    }

//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .coloredShadow(color = Color.Blue),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//
//    }

}

@Composable
private fun DateChip(modifier: Modifier = Modifier, dateSlot: String, isEmptySlot: Boolean) {
    val backgroundChip = if (isEmptySlot) PositiveDateChip else NegativeDateChip

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundChip)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = dateSlot, color = Color.White)
    }
}

@Composable
private fun RoomChip(modifier: Modifier = Modifier, roomName: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(RoomChipBackground)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = roomName, color = RoomNameTextColor)
    }
}

@Composable
private fun TimeBlock(modifier: Modifier = Modifier, timeStart: String, timeEnd: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = timeStart, fontWeight = FontWeight.Black, fontSize = 22.sp)
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_time),
            contentDescription = null,
            tint = GreyIcon
        )
        Text(text = timeEnd, fontWeight = FontWeight.Black, fontSize = 22.sp)

    }
}

@Composable
private fun AvatarBlock(modifier: Modifier = Modifier, owner: Users) {
    Image(
        painter = painterResource(id = R.drawable.avatar1),
        contentDescription = "$owner",
        alpha = 0.7f,
        modifier = modifier
            .size(48.dp)
    )
}

@Preview
@Composable
fun RoomElementsPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateChip(dateSlot = "20 марта", isEmptySlot = true)
        DateChip(dateSlot = "20 марта", isEmptySlot = false)
        RoomChip(roomName = "Java Room")
        TimeBlock(timeStart = "12:15", timeEnd = "13:00")
        AvatarBlock(owner = Users("", "Anna"))
    }
}
