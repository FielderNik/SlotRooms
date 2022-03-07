package com.testing.slotrooms.presentation.addnewslot

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.testing.slotrooms.R
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.buttons.ButtonBlock
import com.testing.slotrooms.utils.dateFormat
import com.testing.slotrooms.utils.timeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddNewSlotScreen(
    slotRoomId: String?,
    appTopBarState: MutableState<AppTopBarState>,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: AddNewSlotViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as AppCompatActivity
    val viewState = viewModel.addNewSlotState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources
    val commentDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources, isNewSlot = slotRoomId == null)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            handleEffect(
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                effect = it,
                resources = resources,
                navController = navController,
                commentDialogState = commentDialogState
            )
        }
    }

    LaunchedEffect(key1 = viewState, block = {
        viewModel.handleEvent(event = AddNewSlotEvent.EnterScreen(slotRoomId))
    })

    when (val state = viewState.value) {
        is AddNewSlotState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AddNewSlotState.OpenSlotDialog -> {
            ChoiceDialogView(
                dialogType = state.dialogType,
                viewModel = viewModel,
                dataList = state.dataList
            )
        }
        is AddNewSlotState.DisplaySlotState -> {
            RoomEditBlock(
                slotRoomId = slotRoomId,
                viewModel = viewModel,
                scaffoldState = scaffoldState,
                navController = navController,
                commentDialogState = commentDialogState,
                slotRoom = state.slotRoom,
                activity = activity
            )
        }
    }

}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources, isNewSlot: Boolean) {
    val title = if (isNewSlot) resources.getString(R.string.title_new_slot) else resources.getString(R.string.title_edit_slot)
    appTopBarState.value = appTopBarState.value.copy(
        title = title,
        isShowBack = true,
        isShowFilter = false
    )
}


@Composable
fun RoomEditBlock(
    slotRoomId: String?,
    viewModel: AddNewSlotViewModel,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    commentDialogState: MutableState<Boolean>,
    slotRoom: SlotRoom,
    activity: AppCompatActivity
) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SlotContentView(title = stringResource(id = R.string.title_room), content = slotRoom.room.name, onClick = {
            viewModel.onRoomSelectRequest()
        })
        SlotDateView(
            title = stringResource(id = R.string.title_begin),
            dateTimeMillis = slotRoom.beginDateTime,
            isBegin = true,
            viewModel = viewModel,
            activity = activity
        )
        SlotDateView(
            title = stringResource(id = R.string.title_end),
            dateTimeMillis = slotRoom.endDateTime,
            isBegin = false,
            viewModel = viewModel,
            activity = activity
        )
        SlotContentView(title = stringResource(id = R.string.title_owner), content = slotRoom.owner.name, onClick = {
            viewModel.onUsersSelectRequest()
        })
        SlotContentView(title = stringResource(id = R.string.title_comment), content = slotRoom.comments, onClick = {
            viewModel.onRequestComment()
        })

        Spacer(modifier = Modifier.height(16.dp))
        ButtonBlock(
            onSaveClicked = {
                viewModel.handleEvent(AddNewSlotEvent.SaveSlotEvent)
            },
            onCancelClicked = {
                viewModel.handleEvent(AddNewSlotEvent.CancelSlotEvent)
                navController.navigate(Screens.Slots.screenRoute)
            }
        )
    }

    CommentDialog(
        commentDialogState = commentDialogState,
        viewModel = viewModel
    )
}

@Composable
fun <T> ChoiceDialogView(
    dialogType: DialogType,
    viewModel: AddNewSlotViewModel,
    dataList: List<T>,
) {
    if (dialogType == DialogType.ROOM) {
        ChoiceRoomDialog<Rooms>(
            viewModel = viewModel,
            dialogType = dialogType,
            dataList = dataList as List<Rooms>,
        )
    } else {
        ChoiceRoomDialog<Users>(
            viewModel = viewModel,
            dialogType = dialogType,
            dataList = dataList as List<Users>,
        )
    }
}


@Composable
fun SlotContentView(title: String, content: String, onClick: () -> Unit) {
    //title
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(top = 8.dp)
    )
    //content
    Text(text = content,
        style = MaterialTheme.typography.h3,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
            .padding(top = 8.dp, bottom = 4.dp)
    )
    Divider(modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun SlotDateView(
    title: String,
    dateTimeMillis: Long = 0L,
    isBegin: Boolean,
    viewModel: AddNewSlotViewModel,
    activity: AppCompatActivity
) {
    //title
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(top = 8.dp)
    )
    //content
    Row() {
        Text(text = dateTimeMillis.dateFormat(),
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    showDatePicker(
                        activity = activity,
                        isBegin = isBegin,
                        viewModel = viewModel
                    )
                }
                .padding(top = 12.dp, bottom = 8.dp, end = 16.dp)
        )

        Text(text = dateTimeMillis.timeFormat(),
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    showTimePicker(
                        activity = activity,
                        isBegin = isBegin,
                        viewModel = viewModel
                    )
                }
                .padding(top = 12.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        )
    }
    Divider()
}

private fun showDatePicker(
    activity: AppCompatActivity,
    isBegin: Boolean,
    viewModel: AddNewSlotViewModel
) {
    val datePicker = MaterialDatePicker.Builder.datePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener {
        viewModel.updateDate(isBegin = isBegin, date = it)

    }
    datePicker.addOnCancelListener {
//        onDismiss.invoke()
    }
    datePicker.addOnNegativeButtonClickListener {
//        onDismiss.invoke()
    }
}


private fun showTimePicker(
    activity: AppCompatActivity,
    isBegin: Boolean,
    viewModel: AddNewSlotViewModel
) {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build()

    timePicker.show(activity.supportFragmentManager, timePicker.toString())
    timePicker.addOnPositiveButtonClickListener {
        viewModel.updateTime(hour = timePicker.hour, minutes = timePicker.minute, isBegin = isBegin)
//        updatedTime(timePicker.hour, timePicker.minute)
    }
    timePicker.addOnCancelListener {
//        onDismiss.invoke()
    }

    timePicker.addOnNegativeButtonClickListener {
//        onDismiss.invoke()
    }

}


private fun handleEffect(
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    effect: Effects?,
    resources: Resources,
    navController: NavHostController,
    commentDialogState: MutableState<Boolean>
) {
    effect?.let { effectEvent ->
        when (effectEvent) {
            is AddNewSlotEvent.AddNewSlotError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(effectEvent.message)
                }
            }
            is AddNewSlotEvent.DateTimeError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_slot_date_time))
                }
            }
            AddNewSlotEvent.OwnerEmptyError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_slot_owner_is_empty))
                }
            }
            AddNewSlotEvent.RoomEmptyError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_slot_room_is_empty))
                }
            }
            is AddNewSlotEvent.GetRoomsError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_get_rooms))
                }
            }
            is AddNewSlotEvent.GetUsersError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_get_users))
                }
            }
            is AddNewSlotEvent.SaveSlotError -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_slot_is_busy))
                }
            }
            is AddNewSlotEvent.SlotSavedSuccess -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.notice_slot_saved_success))
                    navController.navigate(Screens.Slots.screenRoute)
                }
            }
            is AddNewSlotEvent.OpenCommentDialog -> {
                commentDialogState.value = true
            }
        }
    }
}


@Preview
@Composable
fun AddNewSlotScreen_Preview() {
    val topBarState = remember {
        mutableStateOf(AppTopBarState())
    }
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    AddNewSlotScreen(slotRoomId = null, appTopBarState = topBarState, navController = navController, scaffoldState = scaffoldState)
}