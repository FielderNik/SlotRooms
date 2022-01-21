package com.testing.slotrooms.presentation.addnewslot

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.testing.slotrooms.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddNewSlotScreen(
    isNewSlot: Boolean,
) {
    Scaffold() {
        Column {
            ToolbarAddNewSlot(isNewSlot)
            RoomEditBlock()

        }
    }
}

@Composable
fun ToolbarAddNewSlot(
    isNewSlot: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
            contentDescription = null
        )
        Text(
            text =
            if (isNewSlot) {
                stringResource(id = R.string.title_new_slot)
            } else {
                stringResource(id = R.string.title_edit_slot)
            },
            style = MaterialTheme.typography.h1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoomEditBlock(
    viewModel: AddNewSlotViewModel = viewModel<AddNewSlotViewModelImpl>(),
) {
    val activity = LocalContext.current as AppCompatActivity
    val viewState = viewModel.addNewSlotState.collectAsState()
    val slotRoom = viewModel.slotRoom.collectAsState()

    val beginDateMillis = remember {
        mutableStateOf(0L)
    }

    val choiceDialogState = remember {
        mutableStateOf(SlotDialog())
    }
    when (val state = viewState.value) {
        is AddNewSlotState.OpenSlotDialog -> {
            ChoiceDialogView(dialogType = state.dialogType, viewModel = viewModel)
        }
        is AddNewSlotState.DisplaySlotState -> {
            choiceDialogState.value = SlotDialog(isOpen = false)
        }
        is AddNewSlotState.OpenDatePicker -> {
            showDatePicker2(activity = activity) {
                beginDateMillis.value = it ?: 0L
                if (state.isBegin) {
                    viewModel.handleEvent(AddNewSlotEvent.SelectedBeginDateEvent(it))
                } else {
                    viewModel.handleEvent(AddNewSlotEvent.SelectedEndDateEvent(it))
                }
            }
        }
        is AddNewSlotState.OpenTimePicker -> {
            showTimePicker2(activity = activity) { hour, minute ->
                if (state.isBegin) {
                    viewModel.handleEvent(AddNewSlotEvent.SelectedBeginTimeEvent(hour, minute))
                } else {
                    viewModel.handleEvent(AddNewSlotEvent.SelectedEndTimeEvent(hour, minute))
                }
            }
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

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SlotContentView(title = "Room", content = slotRoom.value.roomName, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.OnDialogClicked(DialogType.ROOM))

        })
        SlotDateView(
            title = "Begin",
            date = slotRoom.value.beginDate,
            time = slotRoom.value.beginTime,
            onDateClick = {
                viewModel.handleEvent(AddNewSlotEvent.DatePickerClicked(isBegin = true))
            },
            onTimeClick = {
                viewModel.handleEvent(AddNewSlotEvent.TimePickerClicked(isBegin = true))
            })

        SlotDateView(
            title = "End",
            date = slotRoom.value.endDate,
            time = slotRoom.value.endTime,
            onDateClick = {
                viewModel.handleEvent(AddNewSlotEvent.DatePickerClicked(isBegin = false))
/*                showDatePicker2(activity = activity) {
                    endDateMillis.value = it ?: 0L
                    if (endDateMillis.value < beginDateMillis.value) {
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar("Fire in the home", "go")
                            when(result) {
                                SnackbarResult.Dismissed -> {}

                                        SnackbarResult.ActionPerformed -> {}
                            }
                        }
                    } else {
                        endDate.value = it.dateFormat(template = dateTemplate)
                    }
                }*/
            },
            onTimeClick = {
                viewModel.handleEvent(AddNewSlotEvent.TimePickerClicked(isBegin = false))
            })

        SlotContentView(title = "Owner", content = slotRoom.value.owner, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.OnDialogClicked(DialogType.OWNER))
        })

        SlotContentView(title = "Comment", content = slotRoom.value.comments, onClick = {
            viewModel.handleEvent(AddNewSlotEvent.CommentClicked)
        })
    }



    SnackbarHost(hostState = snackbarHostState)

    LaunchedEffect(key1 = viewState, block = {
        viewModel.handleEvent(event = AddNewSlotEvent.EnterScreen)
    })
}

@Composable
fun ChoiceDialogView(
    dialogType: DialogType,
    viewModel: AddNewSlotViewModel
) {
    if (dialogType == DialogType.ROOM) {
        ChoiceRoomDialog(
            onConfirmClicked = {
                viewModel.handleEvent(AddNewSlotEvent.SelectedRoomEvent(it))
            },
            onDismiss = {
                viewModel.handleEvent(AddNewSlotEvent.OnDialogDismissClicked)
            },
            dialogType = dialogType
        )
    } else {
        ChoiceRoomDialog(
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
fun AddNewSlotDisplayView(
    viewState: AddNewSlotState.DisplaySlotState,
    onChoiceRoomClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SlotContentView(title = "Room", content = viewState.slotRoom.roomName, onClick = onChoiceRoomClicked)

        SlotContentView(title = "Owner", content = viewState.slotRoom.owner, onClick = {})
        SlotContentView(title = "Comment", content = viewState.slotRoom.comments, onClick = {})
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
            .padding(top = 16.dp, bottom = 8.dp)
    )
    Divider()
}

@Composable
fun SlotDateView(
    title: String,
    date: String,
    time: String,
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
        Text(text = date,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    onDateClick.invoke()
                }
                .padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
        )

        Text(text = time,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    onTimeClick.invoke()
                }
                .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        )
    }
    Divider()
}

private fun showDatePicker(
    context: Context,
    updatedDate: (String?) -> Unit
) {
    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(year, monthOfYear, dayOfMonth)
            val formatDate = SimpleDateFormat("dd MMM yyyy")
            val date = formatDate.format(calendar.time)
            updatedDate(date)
        }, year, month, day
    )

    datePicker.show()

}

private fun showDatePicker2(
    activity: AppCompatActivity,
    updatedDate: (Long) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener {
        updatedDate(it)
    }
}

private fun showTimePicker(
    context: Context,
    updatedTime: (String?) -> Unit
) {
    val hours: Int
    val minutes: Int
    val calendar = Calendar.getInstance()
    hours = calendar.get(Calendar.HOUR)
    minutes = calendar.get(Calendar.MINUTE)

    val timePicker = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minutes: Int ->

        }, hours, minutes, true
    )
    timePicker.show()
}

private fun showTimePicker2(
    activity: AppCompatActivity,
    updatedTime: (Int, Int) -> Unit
) {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setInputMode(INPUT_MODE_KEYBOARD)
        .build()

    timePicker.show(activity.supportFragmentManager, timePicker.toString())
    timePicker.addOnPositiveButtonClickListener {
        updatedTime(timePicker.hour, timePicker.minute)
    }

}

@Composable
fun ShowError(text: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold() {
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = text
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {}
            }
        }
    }
}

@Preview
@Composable
fun AddNewSlotScreen_Preview() {
    AddNewSlotScreen(isNewSlot = true)
}