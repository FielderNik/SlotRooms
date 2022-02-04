package com.testing.slotrooms.presentation.slots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.ui.theme.GreenMain
import com.testing.slotrooms.ui.theme.MainFont
import com.testing.slotrooms.ui.theme.YellowMain
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SlotsScreen(
    viewModel: SlotsViewModel = hiltViewModel(),
    navController: NavHostController,
    appTopBarState: MutableState<AppTopBarState>
) {
    val slotsScreenState = viewModel.slotsScreenState.collectAsState()
    val slotsScreenEffect = viewModel.slotsScreenEffect.collectAsState()

    LaunchedEffect(Unit) {
        appTopBarState.value = appTopBarState.value.copy(title = "Hello world")

    }

    LaunchedEffect(slotsScreenState) {
        viewModel.handleEvent(SlotsScreenEvent.SlotsEnterScreenEvent)
    }

    LaunchedEffect(slotsScreenEffect) {

    }

    Column() {
        ToolbarSlots()


        when (val state = slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Empty")
                }
            }
            is SlotsScreenState.SlotsLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SlotsScreenState.SlotsSuccess -> {
                SlotsList(slots = state.slots, navController = navController)
            }
        }
    }



}


@Composable
fun ToolbarSlots() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.title_slots),
            style = MaterialTheme.typography.h1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
            contentDescription = null
        )
    }
}

@Composable
fun SlotsList(slots: List<SlotRoom>, navController: NavController) {
    val calendar = Calendar.getInstance(Locale.ROOT)
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(slots) { slot ->
            SlotRow(slot = slot, calendar = calendar, navController = navController)
        }
    }
}

@Composable
fun SlotRow(slot: SlotRoom, calendar: Calendar, navController: NavController) {
    calendar.get(Calendar.HOUR)
    val timeTextStart = SimpleDateFormat("HH.mm").format(slot.beginDateTime)
    val timeTextFinish = SimpleDateFormat("HH.mm").format(slot.endDateTime)
    val timeMeeting = "$timeTextStart - $timeTextFinish"
    val dateMeeting = SimpleDateFormat("dd MMMM").format(slot.endDateTime)
    val backgroundColorSlot = if (slot.owner.name.isNullOrEmpty()) {
        GreenMain
    } else {
        YellowMain
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(backgroundColorSlot)
            .clickable {
                navController.navigate(Screens.AddNewSlotScreen.passIsNewSlot(false))
            }
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(start = 16.dp, top = 16.dp, bottom = 12.dp)
        ) {
            Text(text = timeMeeting, style = MaterialTheme.typography.h3)
            Text(
                text = dateMeeting,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp)
        ) {
            Text(
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
                    .padding(top = 16.dp)
                    .size(24.dp)

            )
        }
    }

}

