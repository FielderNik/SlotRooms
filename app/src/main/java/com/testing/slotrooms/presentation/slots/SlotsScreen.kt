package com.testing.slotrooms.presentation.slots

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.testing.slotrooms.R
import com.testing.slotrooms.domain.slots.SlotsModel
import com.testing.slotrooms.ui.theme.GreenMain
import com.testing.slotrooms.ui.theme.YellowMain
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SlotsScreen(
    viewModel: SlotsViewModel
) {

    val slots = viewModel.slots
    Column() {
        Text(
            text = stringResource(id = R.string.title_slots),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(20.dp)
        )
        SlotsList(slots = slots)
    }
}

@Composable
fun SlotsList(slots: List<SlotsModel>) {
    val calendar = Calendar.getInstance(Locale.ROOT)
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(slots) { slot ->
            SlotRow(slot = slot, calendar = calendar)
        }
    }
}

@Composable
fun SlotRow(slot : SlotsModel, calendar: Calendar) {
    calendar.get(Calendar.HOUR)
    val timeTextStart = SimpleDateFormat("HH.mm").format(slot.startMeeting)
    val timeTextFinish = SimpleDateFormat("HH.mm").format(slot.finishMeeting)
    val timeMeeting = "$timeTextStart - $timeTextFinish"
    val dateMeeting = SimpleDateFormat("dd MMMM").format(slot.finishMeeting)
    val backgroundColorSlot = if (slot.owner.name.isNullOrEmpty()) {
        GreenMain
    } else {
        YellowMain
    }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 0.dp).background(backgroundColorSlot)) {
        Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically)) {
            Text(text = timeMeeting, style = MaterialTheme.typography.h3)
            Text(text = dateMeeting, style = MaterialTheme.typography.h4)
        }
        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(text = slot.roomName)
            Text(text = slot.owner.name)
        }
    }
//    Column() {
//        Row() {
//            Text(text = timeMeeting)
//            Text(text = dateMeeting)
//        }
//        Row() {
//            Text(text = slot.roomName)
//            Text(text = slot.owner.name)
//        }
//    }
}