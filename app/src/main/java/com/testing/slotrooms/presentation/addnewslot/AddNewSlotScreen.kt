package com.testing.slotrooms.presentation.addnewslot

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.testing.slotrooms.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddNewSlotScreen(
    isNewSlot: Boolean
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
fun RoomEditBlock() {
    val choiceRoomDialog = remember { mutableStateOf(false) }
    val roomName = remember { mutableStateOf("Choice room") }
    val beginDate = remember { mutableStateOf("5 Jan 2022") }
    val beginTime = remember { mutableStateOf("11 : 00") }
    val endDate = remember { mutableStateOf("5 Jan 2022") }
    val endTime = remember { mutableStateOf("12 : 00") }
    val owner = remember { mutableStateOf("Pavel Ivanov") }
    val comment = remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = LocalContext.current as AppCompatActivity
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SlotContentView(title = "Room", content = roomName, onClick = { choiceRoomDialog.value = true })
        SlotDateView(
            title = "Begin",
            date = beginDate,
            time = beginTime,
            onDateClick = {
                showDatePicker(context = context) {
                    beginDate.value = it.toString()
                }
            },
            onTimeClick = {
                showTimePicker2(activity = activity) { hour, minute ->
                    beginTime.value = "$hour : $minute"
                }
            })

        SlotDateView(
            title = "End",
            date = endDate,
            time = endTime,
            onDateClick = {
                showDatePicker2(activity = activity) {

                }
//                showDatePicker(context = context) {
//                    endDate.value = it.toString()
//                }
            },
            onTimeClick = {
                showTimePicker2(activity = activity) { hour, minute ->
                    endTime.value = "$hour : $minute"
                }
            })

//        SlotContentView(title = "Begin", content = beginDate, onClick = {
//            showDatePicker(context = context) {
//                beginDate.value = it.toString()
//            }
//        })

//        SlotContentView(title = "End", content = endTime, onClick = {
//            showDatePicker(context = context) {
//                endTime.value = it.toString()
//            }
//        })
        SlotContentView(title = "Owner", content = owner, onClick = {
            showTimePicker2(activity = activity) { hour, minute ->
                owner.value = "$hour : $minute"
            }
        })
        SlotContentView(title = "Comment", content = comment, onClick = {})
    }

    ChoiceRoomDialog(
        onConfirmClicked = {
            choiceRoomDialog.value = false
            roomName.value = it
        },
        onDismiss = {
            choiceRoomDialog.value = false
        },
        dialogState = choiceRoomDialog
    )
}

@Composable
fun SlotContentView(title: String, content: MutableState<String>, onClick: () -> Unit) {
    //title
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(top = 8.dp)
    )
    //content
    Text(text = content.value,
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
    date: MutableState<String>,
    time: MutableState<String>,
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
        Text(text = date.value,
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .clickable {
                    onDateClick.invoke()
                }
                .padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
        )

        Text(text = time.value,
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
    updatedDate: (Long?) -> Unit
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
    updatedTime: (Int?, Int?) -> Unit
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

@Preview
@Composable
fun AddNewSlotScreen_Preview() {
    AddNewSlotScreen(isNewSlot = true)
}