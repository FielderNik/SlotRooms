package com.testing.slotrooms.presentation.addnewslot

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

    abstract val rooms: List<String>
    override fun handleEvent(event: AddNewSlotEvent) {}
}

class AddNewSlotViewModelImpl : AddNewSlotViewModel(), EventHandler<AddNewSlotEvent> {
    override val rooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")
    private val _addNewSlotState: MutableStateFlow<AddNewSlotState> = MutableStateFlow(AddNewSlotState.EmptyState)
    override val addNewSlotState: StateFlow<AddNewSlotState> = _addNewSlotState

    private val _slotRoom: MutableStateFlow<SlotRoom> = MutableStateFlow(SlotRoom())
    override val slotRoom: StateFlow<SlotRoom> = _slotRoom

    private val dateTemplate = "dd MMM yyyy"


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
        when(event) {
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
        when(event) {
            is AddNewSlotEvent.SelectedBeginTimeEvent -> {
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(beginTime = "${event.beginTimeHour} : ${event.beginTimeMinutes}"))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
            is AddNewSlotEvent.SelectedEndTimeEvent -> {
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(endTime = "${event.endTimeHour} : ${event.endTimeMinutes}"))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenDatePicker) {
        when(event) {
            is AddNewSlotEvent.SelectedBeginDateEvent -> {
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(beginDate = event.beginDateMillis.dateFormat(dateTemplate)))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
            is AddNewSlotEvent.SelectedEndDateEvent -> {
                viewModelScope.launch {
                    _slotRoom.emit(_slotRoom.value.copy(endDate = event.endDateMillis.dateFormat(dateTemplate)))
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
        }
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
        }
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

    override fun handleEvent(event: AddNewSlotEvent) {
    }
}