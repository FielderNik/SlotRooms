package com.testing.slotrooms.presentation.slots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.ui.theme.DeleteIconColor
import com.testing.slotrooms.ui.theme.MainFont
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SlotCard(
    viewModel: SlotsViewModel,
    slot: SlotRoom,
    navController: NavController,
    filter: SlotFilter?
) {
    val squareSize = (-120).dp
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ ->
                    FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal,
                reverseDirection = false
            )
    ) {
        Box(
            modifier = Modifier
                .padding(end = 32.dp)
                .height(120.dp)
                .fillMaxWidth()
            ,
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = {
                    viewModel.deleteSlot(slot = slot, filter = filter)
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_outline),
                    contentDescription = null,
                    tint = DeleteIconColor
                )
            }
        }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        swipeableState.offset.value.roundToInt(), 0
                    )
                }
                .background(Color.White)
            ,
        ) {
            SlotCardItemNew(slot = slot, navController = navController)
        }
    }
}


@Composable
private fun SlotRow(slot: SlotRoom, navController: NavController) {
    val timeFormat = SimpleDateFormat("HH.mm")
    val timeTextStart = timeFormat.format(slot.beginDateTime)
    val timeTextFinish = timeFormat.format(slot.endDateTime)
    val timeMeeting = "$timeTextStart - $timeTextFinish"
    val dateMeeting = SimpleDateFormat("dd MMMM").format(slot.endDateTime)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 16.dp)
            .clickable {
                navController.navigate(Screens.AddNewSlotScreen.passSlotRoomId(slotRoomId = slot.id))
            }
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(start = 16.dp)
                .fillMaxHeight()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = timeMeeting,
                style = MaterialTheme.typography.h3
            )
            Text(
                text = dateMeeting,
                style = MaterialTheme.typography.h4,
            )
        }
        Column(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxHeight()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = slot.room.name,
                style = TextStyle(
                    fontFamily = MainFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Image(
                painter = painterResource(id = R.drawable.avatar1),
                contentDescription = "${slot.owner}",
                modifier = Modifier
                    .align(Alignment.End)
                    .size(24.dp)

            )
        }
    }

}