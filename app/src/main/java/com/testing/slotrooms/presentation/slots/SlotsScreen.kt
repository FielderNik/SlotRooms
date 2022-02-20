package com.testing.slotrooms.presentation.slots

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.LoadingSlots
import com.testing.slotrooms.ui.theme.GreenMain
import com.testing.slotrooms.ui.theme.MainFont
import com.testing.slotrooms.ui.theme.YellowMain
import java.text.SimpleDateFormat

@Composable
fun SlotsScreen(
    viewModel: SlotsViewModel = hiltViewModel(),
    navController: NavHostController,
    appTopBarState: MutableState<AppTopBarState>,
    scaffoldState: ScaffoldState,
    filter: SlotFilter? = null,
) {
    val slotsScreenState = viewModel.slotsScreenState.collectAsState()
//    val slotsScreenEffect = viewModel.slotsScreenEffect.collectAsState()
    val resources = LocalContext.current.resources

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources, navController = navController)
    }

    LaunchedEffect(slotsScreenState) {
        viewModel.handleEvent(SlotsScreenEvent.SlotsEnterScreenEvent(filter))
    }

    LaunchedEffect(Unit) {
        viewModel.effectChannel.collect {
            handleEffect(
                scaffoldState = scaffoldState,
                effect = it,
                resources = resources,
                navController = navController
            )
            viewModel.resetErrorStatus()
        }
    }

    Column {

        when (val state = slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Empty")
                }
            }
            is SlotsScreenState.SlotsLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingSlots()
                }
            }
            is SlotsScreenState.SlotsSuccess -> {
                SlotsList(slots = state.slots, navController = navController)
            }

            is SlotsScreenState.FilterOpened -> {
            }
        }
    }


}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources, navController: NavHostController) {
    appTopBarState.value = appTopBarState.value.copy(
        title = resources.getString(R.string.title_slots),
        isShowBack = false,
        isShowFilter = true,
        onFilterClicked = { navController.navigate(Screens.Filter.screenRoute) }
    )
}


@Composable
fun SlotsList(slots: List<SlotRoom>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(slots) { slot ->
            SlotRow(slot = slot, navController = navController)
        }
    }
}

@Composable
fun SlotRow(slot: SlotRoom, navController: NavController) {
    val timeFormat = SimpleDateFormat("HH.mm")
    val timeTextStart = timeFormat.format(slot.beginDateTime)
    val timeTextFinish = timeFormat.format(slot.endDateTime)
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
                navController.navigate(Screens.AddNewSlotScreen.passSlotRoomId(slotRoomId = slot.id))
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

private suspend fun handleEffect(
    scaffoldState: ScaffoldState,
    effect: SlotsScreenEffect?,
    resources: Resources,
    navController: NavHostController
) {
    when (effect) {
        is SlotsScreenEffect.ErrorLoading -> {
            scaffoldState.snackbarHostState.showSnackbar(effect.exception.message ?: "Error")
        }

    }
}

