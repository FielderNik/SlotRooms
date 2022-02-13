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
    scaffoldState: ScaffoldState
) {
    val resources = LocalContext.current.resources

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources, isNewSlot = slotRoomId == null)
    }
    RoomEditBlock(slotRoomId = slotRoomId, viewModel = hiltViewModel(), scaffoldState = scaffoldState, navController = navController)

}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources, isNewSlot: Boolean) {
    val title = if (isNewSlot) resources.getString(R.string.title_new_slot) else resources.getString(R.string.title_edit_slot)
    appTopBarState.value = appTopBarState.value.copy(
        title = title,
        isShowBack = true
    )
}


@Composable
fun RoomEditBlock(
    slotRoomId: String?,
    viewModel: AddNewSlotViewModelImpl,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    val activity = LocalContext.current as AppCompatActivity
    val viewState = viewModel.addNewSlotState.collectAsState()
    val slotRoom = viewModel.slotRoom.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    when (val state = viewState.value) {
        is AddNewSlotState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AddNewSlotState.OpenSlotDialog -> {
            ChoiceDialogView(dialogType = state.dialogType, viewModel = viewModel)
        }
        is AddNewSlotState.DisplaySlotState -> {
        }
        is AddNewSlotState.OpenDatePicker -> {
            showDatePicker(
                activity = activity,
                updatedDate = {
                    if (state.isBegin) {
                        viewModel.handleEvent(AddNewSlotEvent.SelectedBeginDateEvent(it))
                    } else {
                        viewModel.handleEvent(AddNewSlotEvent.SelectedEndDateEvent(it))
                    }
                },
                onDismiss = {
                    viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
                })
        }
        is AddNewSlotState.OpenTimePicker -> {
            showTimePicker(
                activity = activity,
                updatedTime = { hour, minute ->
                    if (state.isBegin) {
                        viewModel.handleEvent(AddNewSlotEvent.SelectedBeginTimeEvent(hour, minute))
                    } else {
                        viewModel.handleEvent(AddNewSlotEvent.SelectedEndTimeEvent(hour, minute))
                    }
                },
                onDismiss = {
                    viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
                })
        }
        is AddNewSlotState.OpenCommentDialog -> {
            CommentDialog(
                onConfirmClicked = {
                    viewModel.handleEvent(AddNewSlotEvent.CommentSubmittedEvent(it))
                },
                onDismissClicked = {
                    viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
                }
            )
        }

    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            handleEffect(
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                effect = it,
                resources = resources,
                navController = navController
            )
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SlotContentView(title = stringResource(id = R.string.title_room), content = slotRoom.value.room.name, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.OnDialogClicked(DialogType.ROOM))
        })
        SlotDateView(
            title = stringResource(id = R.string.title_begin),
            dateTimeMillis = slotRoom.value.beginDateTime,
            onDateClick = {
                viewModel.handleEvent(AddNewSlotEvent.DatePickerClicked(isBegin = true))
            },
            onTimeClick = {
                viewModel.handleEvent(AddNewSlotEvent.TimePickerClicked(isBegin = true))
            })
        SlotDateView(
            title = stringResource(id = R.string.title_end),
            dateTimeMillis = slotRoom.value.endDateTime,
            onDateClick = {
                viewModel.handleEvent(AddNewSlotEvent.DatePickerClicked(isBegin = false))
            },
            onTimeClick = {
                viewModel.handleEvent(AddNewSlotEvent.TimePickerClicked(isBegin = false))
            })
        SlotContentView(title = stringResource(id = R.string.title_owner), content = slotRoom.value.owner.name, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.OnDialogClicked(DialogType.OWNER))
        })
        SlotContentView(title = stringResource(id = R.string.title_comment), content = slotRoom.value.comments, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.CommentClicked)
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

    LaunchedEffect(key1 = viewState, block = {
        viewModel.handleEvent(event = AddNewSlotEvent.EnterScreen(slotRoomId))
    })
}

@Composable
fun ChoiceDialogView(
    dialogType: DialogType,
    viewModel: AddNewSlotViewModelImpl
) {
    if (dialogType == DialogType.ROOM) {
        ChoiceRoomDialog<Rooms>(
            viewModel = viewModel,
            onConfirmClicked = {
                viewModel.handleEvent(AddNewSlotEvent.SelectedRoomEvent(it))
            },
            onDismiss = {
                viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
            },
            dialogType = dialogType
        )
    } else {
        ChoiceRoomDialog<Users>(
            viewModel = viewModel,
            onConfirmClicked = {
                viewModel.handleEvent(AddNewSlotEvent.SelectedOwnerEvent(it))
            },
            onDismiss = {
                viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
            },
            dialogType = dialogType
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
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
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
                    onDateClick.invoke()
                }
                .padding(top = 12.dp, bottom = 8.dp, end = 16.dp)
        )

        Text(text = dateTimeMillis.timeFormat(),
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    onTimeClick.invoke()
                }
                .padding(top = 12.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        )
    }
    Divider()
}

private fun showDatePicker(
    activity: AppCompatActivity,
    updatedDate: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
    datePicker.addOnCancelListener {
        onDismiss.invoke()
    }
    datePicker.addOnNegativeButtonClickListener {
        onDismiss.invoke()
    }
}


private fun showTimePicker(
    activity: AppCompatActivity,
    updatedTime: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build()

    timePicker.show(activity.supportFragmentManager, timePicker.toString())
    timePicker.addOnPositiveButtonClickListener {
        updatedTime(timePicker.hour, timePicker.minute)
    }
    timePicker.addOnCancelListener {
        onDismiss.invoke()
    }

    timePicker.addOnNegativeButtonClickListener {
        onDismiss.invoke()
    }

}


private fun handleEffect(
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    effect: Effects?,
    resources: Resources,
    navController: NavHostController
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