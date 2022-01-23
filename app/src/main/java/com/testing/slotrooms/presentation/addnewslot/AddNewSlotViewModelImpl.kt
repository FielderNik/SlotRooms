package com.testing.slotrooms.presentation.addnewslot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.utils.dateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class AddNewSlotViewModel : ViewModel(), EventHandler<AddNewSlotEvent> {
    abstract val addNewSlotState: StateFlow<AddNewSlotState>
    abstract val slotRoom: StateFlow<SlotRoom>
    abstract val effect: StateFlow<Effects?>

    abstract val rooms: List<String>
    override fun handleEvent(event: AddNewSlotEvent) {}
    open suspend fun resetErrorStatus() {}
}

class AddNewSlotViewModelImpl : AddNewSlotViewModel(), EventHandler<AddNewSlotEvent> {
    override val rooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")

    private val currentDate = SlotRoom.getCurrentDate()
    private val _addNewSlotState: MutableStateFlow<AddNewSlotState> = MutableStateFlow(AddNewSlotState.EmptyState)
    override val addNewSlotState: StateFlow<AddNewSlotState> = _addNewSlotState

    private val _slotRoom: MutableStateFlow<SlotRoom> = MutableStateFlow(SlotRoom(beginDateTime = currentDate, endDateTime = currentDate))
    override val slotRoom: StateFlow<SlotRoom> = _slotRoom

    private val _effect: MutableStateFlow<Effects?> = MutableStateFlow(null)
    override val effect: StateFlow<Effects?> = _effect

    private val dateTemplate = "dd MMM yyyy"

    var error = AddNewSlotEvent.AddNewSlotError("--=Error=--")




    override fun handleEvent(event: AddNewSlotEvent) {
        when (val currentState = _addNewSlotState.value) {
            is AddNewSlotState.EmptyState -> reduce(event, currentState)
            is AddNewSlotState.DisplaySlotState -> reduce(event, currentState)
            is AddNewSlotState.OpenSlotDialog -> reduce(event, currentState)
            is AddNewSlotState.OpenDatePicker -> reduce(event, currentState)
            is AddNewSlotState.OpenTimePicker -> reduce(event, currentState)
            is AddNewSlotState.OpenCommentDialog -> reduce(event, currentState)
        }

    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenCommentDialog) {
        when (event) {
            is AddNewSlotEvent.CommentSubmittedEvent -> {
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(comments = event.comment))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
            is AddNewSlotEvent.OnDialogDismissClicked -> {
                viewModelScope.launch {
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenTimePicker) {
        when (event) {
            is AddNewSlotEvent.SelectedBeginTimeEvent -> {
                val hourByMillis = event.beginTimeHour * 3600000L
                val minutesByMillis = event.beginTimeMinutes * 60000L
                val updatedBeginTime = _slotRoom.value.beginDateTime + hourByMillis + minutesByMillis
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(beginTime = "${event.beginTimeHour} : ${event.beginTimeMinutes}"))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                    _slotRoom.value = _slotRoom.value.copy(beginDateTime = updatedBeginTime)
                    Log.d("milk", "begin time by millis: ${_slotRoom.value.beginDateTime}")
                }
            }
            is AddNewSlotEvent.SelectedEndTimeEvent -> {
                val hourByMillis = event.endTimeHour * 3600000L
                val minutesByMillis = event.endTimeMinutes * 60000L
                val updatedEndTime = _slotRoom.value.endDateTime + hourByMillis + minutesByMillis
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(endTime = "${event.endTimeHour} : ${event.endTimeMinutes}"))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                    _slotRoom.value = _slotRoom.value.copy(endDateTime = updatedEndTime)
                    Log.d("milk", "end time by millis: ${_slotRoom.value.endDateTime}")
                }
            }
            is AddNewSlotEvent.OnDialogDismissClicked -> {
                viewModelScope.launch {
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenDatePicker) {
        when (event) {
            is AddNewSlotEvent.SelectedBeginDateEvent -> {
                viewModelScope.launch {
                    if ((event.beginDateMillis > _slotRoom.value.beginDateTime) && _slotRoom.value.beginDateTime != 0L) {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                beginDate = event.beginDateMillis.dateFormat(dateTemplate),
                                beginDateTime = event.beginDateMillis,
                                endDate = event.beginDateMillis.dateFormat(dateTemplate),
                                endDateTime = event.beginDateMillis
                            )
                        )

                        _effect.emit(error.copy("new1"))
                    } else {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                beginDate = event.beginDateMillis.dateFormat(dateTemplate),
                                beginDateTime = event.beginDateMillis
                            )
                        )
                    }
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
            is AddNewSlotEvent.SelectedEndDateEvent -> {
                viewModelScope.launch {
                    if (_slotRoom.value.beginDateTime > event.endDateMillis) {
                        _effect.emit(error.copy("new1"))
                    } else {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                endDate = event.endDateMillis.dateFormat(dateTemplate),
                                endDateTime = event.endDateMillis
                            )
                        )
                    }
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
    }

    override suspend fun resetErrorStatus() {
        _effect.emit(null)
    }



    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenSlotDialog) {
        when (event) {
            is AddNewSlotEvent.SelectedRoomEvent -> {
                fetchDisplayState(event.room)
            }
            is AddNewSlotEvent.SelectedOwnerEvent -> {
                viewModelScope.launch {
                    val currentSlotRoom = _slotRoom.value.copy(owner = event.owner)
                    _slotRoom.emit(currentSlotRoom)
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = currentSlotRoom))
                }
            }
            is AddNewSlotEvent.OnDialogDismissClicked -> {
                viewModelScope.launch {
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.DisplaySlotState) {
        when (event) {
            is AddNewSlotEvent.SelectedRoomEvent -> {
                fetchDisplayState(event.room)
            }
            is AddNewSlotEvent.OnDialogClicked -> {
                openDialog(event.dialogType)
            }
            is AddNewSlotEvent.DatePickerClicked -> {
                openDatePicker(event.isBegin)
            }
            is AddNewSlotEvent.TimePickerClicked -> {
                openTimePicker(event.isBegin)
            }
            is AddNewSlotEvent.CommentClicked -> {
                openCommentDialog()
            }
            is AddNewSlotEvent.SaveSlotEvent -> {
                saveSlot()
            }
            is AddNewSlotEvent.CancelSlotEvent -> {

            }
        }
    }

    private fun saveSlot() {
        viewModelScope.launch {
            if(checkSlot()) {

                Log.d("milk", "OK slot: ${slotRoom.value}")
            } else {
                Log.d("milk", "WRONG! slot: ${slotRoom.value}")
            }

        }
    }

    private suspend fun checkSlot(): Boolean {
        var isChecked = true
        val slotRoom = _slotRoom.value
        if (slotRoom.roomName.isEmpty()) {
            _effect.emit(AddNewSlotEvent.AddNewSlotError("Room is empty"))
            isChecked = false
        }
        if (slotRoom.beginDateTime >= slotRoom.endDateTime) {
            _effect.emit(AddNewSlotEvent.AddNewSlotError("Date is wrong"))
            isChecked = false
        }
        if (slotRoom.owner.isEmpty()) {
            _effect.emit(AddNewSlotEvent.AddNewSlotError("Owner is empty"))
            isChecked = false
        }


        return isChecked
    }

    private fun openCommentDialog() {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.OpenCommentDialog(_slotRoom.value.comments))
        }
    }

    private fun openTimePicker(isBegin: Boolean) {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.OpenTimePicker(isBegin))
        }
    }

    private fun openDatePicker(isBegin: Boolean) {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.OpenDatePicker(isBegin))
        }
    }

    private fun fetchDisplayState(room: String = "") {
        viewModelScope.launch {
            val currentSlotRoom = _slotRoom.value.copy(roomName = room)
            _slotRoom.emit(currentSlotRoom)
            _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = currentSlotRoom))
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.EmptyState) {
        when (event) {
            is AddNewSlotEvent.OnDialogClicked -> {
                openDialog(event.dialogType)
            }
            is AddNewSlotEvent.EnterScreen -> {
                fetchDisplayState()
            }
        }
    }

    private fun openDialog(dialogType: DialogType) {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.OpenSlotDialog(dialogType = dialogType))
        }
    }
}

class AddNewSlotViewModelImplPreview(override val rooms: List<String>) : AddNewSlotViewModel() {
    override val addNewSlotState: StateFlow<AddNewSlotState>
        get() = MutableStateFlow(AddNewSlotState.EmptyState)
    override val slotRoom: StateFlow<SlotRoom>
        get() = TODO("Not yet implemented")
    override val effect: StateFlow<Effects?>
        get() = TODO("Not yet implemented")

    override fun handleEvent(event: AddNewSlotEvent) {
    }
}