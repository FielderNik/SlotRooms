package com.testing.slotrooms.presentation.addnewslot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.domain.usecases.*
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.utils.atStartOfDay
import com.testing.slotrooms.utils.toSlotsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


abstract class AddNewSlotViewModel : ViewModel(), EventHandler<AddNewSlotEvent> {
    abstract val addNewSlotState: StateFlow<AddNewSlotState>
    abstract val slotRoom: StateFlow<SlotRoom>
    abstract val effect: StateFlow<Effects?>
    abstract val rooms: StateFlow<List<Rooms>>
    abstract val owners: StateFlow<List<Users>>

    override fun handleEvent(event: AddNewSlotEvent) {}
}

@HiltViewModel
class AddNewSlotViewModelImpl @Inject constructor(
    private val addDefaultRoomsUseCase: AddDefaultRoomsUseCase,
    private val addDefaultUsersUseCase: AddDefaultUsersUseCase,
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val saveNewSlotUseCase: SaveNewSlotUseCase,
    private val getSlotRoomByIdUseCase: GetSlotRoomByIdUseCase,
) : ViewModel(), EventHandler<AddNewSlotEvent> {
    private val _rooms: MutableStateFlow<List<Rooms>> = MutableStateFlow(listOf(Rooms(UUID.randomUUID().toString(), "Office")))
    val rooms: StateFlow<List<Rooms>> = _rooms

    private val _owners: MutableStateFlow<List<Users>> = MutableStateFlow(listOf(Users(UUID.randomUUID().toString(), "Ivanov Ivan")))
    val owners: StateFlow<List<Users>> = _owners

    private val currentDate = SlotRoom.getCurrentDate()
    private val _addNewSlotState: MutableStateFlow<AddNewSlotState> = MutableStateFlow(AddNewSlotState.EmptyState)
    val addNewSlotState: StateFlow<AddNewSlotState> = _addNewSlotState

    private val _slotRoom: MutableStateFlow<SlotRoom> = MutableStateFlow(SlotRoom(beginDateTime = currentDate, endDateTime = currentDate))
    val slotRoom: StateFlow<SlotRoom> = _slotRoom

    private val effectChannel = Channel<Effects>()
    val effect = effectChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            addDefaultRoomsUseCase.run(None).onSuccess { updatedRooms ->
                launch {
                    _rooms.emit(updatedRooms)
                }
            }

            addDefaultUsersUseCase.run(None).onSuccess { updatedOwners ->
                launch {
                    _owners.emit(updatedOwners)
                }
            }
        }
    }

    override fun handleEvent(event: AddNewSlotEvent) {
        when (val currentState = _addNewSlotState.value) {
            is AddNewSlotState.EmptyState -> reduce(event, currentState)
            is AddNewSlotState.DisplaySlotState -> reduce(event, currentState)
            is AddNewSlotState.OpenSlotDialog -> reduce(event, currentState)
            is AddNewSlotState.OpenDatePicker -> reduce(event, currentState)
            is AddNewSlotState.OpenTimePicker -> reduce(event, currentState)
            is AddNewSlotState.OpenCommentDialog -> reduce(event, currentState)
            is AddNewSlotState.Loading -> reduce(event, currentState)
        }

    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.Loading) {
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
                val updatedBeginTime = getUpdatedTime(event.beginTimeHour, event.beginTimeMinutes, _slotRoom.value.beginDateTime)
                viewModelScope.launch {
                    if (updatedBeginTime > _slotRoom.value.endDateTime) {
                        effectChannel.send(AddNewSlotEvent.DateTimeError)
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                beginDateTime = updatedBeginTime,
                                endDateTime = updatedBeginTime
                            )
                        )
                    } else {
                        _slotRoom.value = _slotRoom.value.copy(beginDateTime = updatedBeginTime)
                    }
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = _slotRoom.value))
                }
            }
            is AddNewSlotEvent.SelectedEndTimeEvent -> {
                val updatedEndTime = getUpdatedTime(event.endTimeHour, event.endTimeMinutes, _slotRoom.value.endDateTime)
                viewModelScope.launch {
                    if (updatedEndTime < _slotRoom.value.beginDateTime) {
                        effectChannel.send(AddNewSlotEvent.DateTimeError)
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                endDateTime = _slotRoom.value.beginDateTime
                            )
                        )
                    } else {
                        _slotRoom.value = _slotRoom.value.copy(endDateTime = updatedEndTime)
                    }
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

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.OpenDatePicker) {
        when (event) {
            is AddNewSlotEvent.SelectedBeginDateEvent -> {
                viewModelScope.launch {
                    if (event.beginDateMillis > _slotRoom.value.endDateTime) {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                beginDateTime = event.beginDateMillis,
                                endDateTime = event.beginDateMillis
                            )
                        )
                        effectChannel.send(AddNewSlotEvent.DateTimeError)
                    } else {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
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
                        effectChannel.send(AddNewSlotEvent.DateTimeError)
                    } else {
                        _slotRoom.emit(
                            _slotRoom.value.copy(
                                endDateTime = event.endDateMillis
                            )
                        )
                    }
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

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.EmptyState) {
        when (event) {
            is AddNewSlotEvent.OnDialogClicked -> {
                openDialog(event.dialogType)
            }
            is AddNewSlotEvent.EnterScreen -> {
                if (event.slotRoomId != null) {
                    fetchEmptyState(event.slotRoomId)
                } else {
                    fetchDisplayState(null)
                }
            }
        }
    }

    private fun fetchEmptyState(slotRoomId: String) {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.Loading)
            withContext(Dispatchers.IO) {
                getSlotRoomByIdUseCase.run(slotRoomId)
            }
                .onSuccess {
                    _slotRoom.emit(
                        _slotRoom.value.copy(
                            id = it.id,
                            room = it.room,
                            owner = it.owner,
                            comments = it.comments,
                            beginDateTime = it.beginDateTime,
                            endDateTime = it.beginDateTime
                        )
                    )
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(it))
                }
                .onFailure {
                    effectChannel.send(AddNewSlotEvent.AddNewSlotError("Печалька!")) // TODO (нужен отдельный эффект)
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(_slotRoom.value))
                }
        }
    }


    private fun saveSlot() {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkSlot()) {
                _slotRoom.value = _slotRoom.value.copy(id = UUID.randomUUID().toString())
                val slotEntity = _slotRoom.value.toSlotsEntity()

                saveNewSlotUseCase.run(slotEntity)
                    .onFailure {
                        effectChannel.send(AddNewSlotEvent.SaveSlotError(it))
                    }
                    .onSuccess {
                        effectChannel.send(AddNewSlotEvent.SlotSavedSuccess)
                    }
            } else {
                Log.d("milk", "WRONG! slot: ${slotRoom.value}")
            }

        }
    }

    private suspend fun checkSlot(): Boolean {
        var isChecked = true
        val slotRoom = _slotRoom.value
        if (slotRoom.room.name.isEmpty()) {
            effectChannel.send(AddNewSlotEvent.RoomEmptyError)
            isChecked = false
        }
        if (slotRoom.beginDateTime >= slotRoom.endDateTime) {
            effectChannel.send(AddNewSlotEvent.DateTimeError)
            isChecked = false
        }
//        if (slotRoom.owner.name.isEmpty()) {
//            _effect.emit(AddNewSlotEvent.OwnerEmptyError)
//            isChecked = false
//        }
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

    private fun fetchDisplayState(room: Rooms?) {
        viewModelScope.launch {
            room?.let {
                val currentSlotRoom = _slotRoom.value.copy(room = room)
                _slotRoom.emit(currentSlotRoom)
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = currentSlotRoom))
            }
        }
    }

    private fun openDialog(dialogType: DialogType) {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.Loading)
            delay(2000)
            when (dialogType) {
                DialogType.ROOM -> {
                    getAllRooms()
                }
                DialogType.OWNER -> {
                    getAllUsers()
                }
            }

        }
    }

    private suspend fun getAllRooms() {
        getAllRoomsUseCase.run(None)
            .onSuccess {
                _rooms.tryEmit(it)
                _addNewSlotState.emit(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.ROOM))
            }
            .onFailure {
                effectChannel.send(AddNewSlotEvent.GetRoomsError(it))
            }
    }

    private suspend fun getAllUsers() {
        getAllUsersUseCase.run(None)
            .onSuccess {
                _owners.tryEmit(it)
                _addNewSlotState.emit(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.OWNER))
            }
            .onFailure {
                effectChannel.send(AddNewSlotEvent.GetUsersError(it))
            }
    }

    private fun getUpdatedTime(hour: Int, minutes: Int, dateTime: Long): Long {
        val hourByMillis = hour * 3600000L
        val minutesByMillis = minutes * 60000L
        val currentStartDay = atStartOfDay(Date(dateTime))?.time ?: 0L
        return currentStartDay + hourByMillis + minutesByMillis
    }
}


class AddNewSlotViewModelImplPreview() : AddNewSlotViewModel() {
    override val rooms: StateFlow<List<Rooms>>
        get() = TODO("Not yet implemented")
    override val owners: StateFlow<List<Users>>
        get() = TODO("Not yet implemented")
    override val addNewSlotState: StateFlow<AddNewSlotState>
        get() = MutableStateFlow(AddNewSlotState.EmptyState)
    override val slotRoom: StateFlow<SlotRoom>
        get() = TODO("Not yet implemented")
    override val effect: StateFlow<Effects?>
        get() = TODO("Not yet implemented")

    override fun handleEvent(event: AddNewSlotEvent) {
    }
}