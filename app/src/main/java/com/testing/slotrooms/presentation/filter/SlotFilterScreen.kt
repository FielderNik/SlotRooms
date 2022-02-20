package com.testing.slotrooms.presentation.filter

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.material.datepicker.MaterialDatePicker
import com.testing.slotrooms.R
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.addnewslot.DialogType
import com.testing.slotrooms.presentation.addnewslot.SlotContentView
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.LoadingSlots
import com.testing.slotrooms.presentation.views.buttons.ButtonBlock
import com.testing.slotrooms.presentation.views.dialogs.ChoiceDialog
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SlotFilterScreen(
    viewModel: SlotFilterViewModel = hiltViewModel(),
    navController: NavHostController,
    appTopBarState: MutableState<AppTopBarState>,
    scaffoldState: ScaffoldState,
) {
    val resources = LocalContext.current.resources
    val slotFilterState = viewModel.slotFilterState.collectAsState()

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources, viewModel = viewModel)
    }

    when (val state = slotFilterState.value) {
        SlotFilterState.EmptyFilterState -> {

        }
        SlotFilterState.FilterLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingSlots()
            }
        }
        is SlotFilterState.ResultFilterState -> {
            SlotFilterContent(
                filter = state.slotFilter,
                viewModel = viewModel,
                navController = navController
            )
        }
        is SlotFilterState.ChoiceDialogState.RoomDialogOpened -> {
            ChoiceDialog<Rooms>(
                onDismiss = {
                    viewModel.handleEvent(SlotFilterEvent.RoomDialogEvent.RoomDialogCanceled)
                },
                onConfirmClicked = {
                    viewModel.handleEvent(SlotFilterEvent.RoomDialogEvent.RoomDialogConfirmed(it))
                },
                dialogType = DialogType.ROOM,
                dataList = state.rooms
            )
        }
        is SlotFilterState.ChoiceDialogState.UserDialogOpened -> {
            ChoiceDialog<Users>(
                onDismiss = {
                    viewModel.handleEvent(SlotFilterEvent.UserDialogEvent.UserDialogCanceled)
                },
                onConfirmClicked = {
                    viewModel.handleEvent(SlotFilterEvent.UserDialogEvent.UserDialogConfirmed(it))
                },
                dialogType = DialogType.OWNER,
                dataList = state.users
            )
        }
    }

}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources, viewModel: SlotFilterViewModel) {
    appTopBarState.value = appTopBarState.value.copy(
        title = resources.getString(R.string.title_slot_filter),
        isShowBack = true,
        isShowFilter = false,
    )
}

@Composable
private fun SlotFilterContent(filter: SlotFilter?, viewModel: SlotFilterViewModel, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            FilterContentBlock(filter = filter, viewModel = viewModel)
            Spacer(modifier = Modifier.height(36.dp))
            ButtonBlock(
                onSaveClicked = {
                    navController.currentBackStackEntry?.arguments?.putParcelable("slotFilter", filter)
                    navController.navigate(Screens.Slots.screenRoute)
                },
                onCancelClicked = {
                    navController.navigate(Screens.Slots.screenRoute)
                }
            )
        }

    }
}


@Composable
private fun FilterContentBlock(filter: SlotFilter?, viewModel: SlotFilterViewModel) {
    val beginDate = getBeginDate(filter)
    val endDate = getEndDate(filter)
    val room = if (filter?.room == null) "" else filter.room.name
    val owner = if (filter?.owner == null) "" else filter.owner.name
    val activity = LocalContext.current as AppCompatActivity

    Box(modifier = Modifier.padding(vertical = 16.dp)) {
        Column {

            SlotContentView(
                title = stringResource(id = R.string.title_date),
                content = "$beginDate - $endDate"
            ) {
                showDatePicker(
                    activity = activity,
                    viewModel = viewModel,
                )
            }

            SlotContentView(
                title = stringResource(id = R.string.title_room),
                content = room
            ) {
                viewModel.handleEvent(SlotFilterEvent.RoomDialogOpen)
            }

            SlotContentView(
                title = stringResource(id = R.string.title_owner),
                content = owner
            ) {
                viewModel.handleEvent(SlotFilterEvent.UserDialogOpen)
            }

        }
    }
}

private fun showDatePicker(
    activity: AppCompatActivity,
    viewModel: SlotFilterViewModel,
) {
    val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener { date ->
        viewModel.handleEvent(SlotFilterEvent.RangeDatePickedEvent(beginDate = date.first, endDate = date.second))
    }
    datePicker.addOnCancelListener {
        viewModel.handleEvent(SlotFilterEvent.RangeDateCanceledEvent)
    }
    datePicker.addOnNegativeButtonClickListener {
        viewModel.handleEvent(SlotFilterEvent.RangeDateCanceledEvent)
    }
}

private fun getBeginDate(filter: SlotFilter?): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    return if (filter?.beginDate == null) {
        ""
    } else {
        dateFormat.format(filter.beginDate)
    }
}

private fun getEndDate(filter: SlotFilter?): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    return if (filter?.endDate == null) {
        ""
    } else {
        dateFormat.format(filter.endDate)
    }
}


/*
@Preview
@Composable
private fun SlotFilterPreview() {
    val navController = NavHostController()
    

    SlotFilterScreen(
        viewModel = hiltViewModel(),
    navController: NavHostController,
    appTopBarState: MutableState<AppTopBarState>,
    scaffoldState: ScaffoldState,
    )

}*/
