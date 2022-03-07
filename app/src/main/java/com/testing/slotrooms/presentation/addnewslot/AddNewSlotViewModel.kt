package com.testing.slotrooms.presentation.addnewslot

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddNewSlotViewModel @Inject constructor(
    private val addDefaultRoomsUseCase: AddDefaultRoomsUseCase,
    private val addDefaultUsersUseCase: AddDefaultUsersUseCase,
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val saveNewSlotUseCase: SaveNewSlotUseCase,
    private val getSlotRoomByIdUseCase: GetSlotRoomByIdUseCase,
) : ViewModel(), EventHandler<AddNewSlotEvent> {

    private val currentDate = SlotRoom.getCurrentDate()
    private var slot = SlotRoom(beginDateTime = currentDate, endDateTime = currentDate)

    private val _effect = Channel<Effects>()
    val effect = _effect.receiveAsFlow()

    private val _addNewSlotState: MutableStateFlow<AddNewSlotState> = MutableStateFlow(AddNewSlotState.DisplaySlotState(slot))
    val addNewSlotState: StateFlow<AddNewSlotState> = _addNewSlotState

    override fun handleEvent(event: AddNewSlotEvent) {
        when (val currentState = _addNewSlotState.value) {
            is AddNewSlotState.DisplaySlotState -> reduce(event, currentState)
            AddNewSlotState.Loading -> {}
            is AddNewSlotState.OpenSlotDialog -> {}
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.DisplaySlotState) {
        when (event) {
            is AddNewSlotEvent.SaveSlotEvent -> {
                saveSlot()
            }
            is AddNewSlotEvent.CancelSlotEvent -> {

            }
            is AddNewSlotEvent.CommentSubmittedEvent -> {
                updateComment(event.comment)
            }
            is AddNewSlotEvent.EnterScreen -> {
                event.slotRoomId?.let {
                    fetchEmptyState(it)
                }
            }
        }
    }


    fun updateComment(comment: String) {
        slot = slot.copy(comments = comment)
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slot))
        }
    }

    fun updateDate(date: Long, isBegin: Boolean) {
        if (isBegin) {
            slot = slot.copy(beginDateTime = date)
            viewModelScope.launch {
                if (date > slot.endDateTime) {
                    slot = slot.copy(beginDateTime = date, endDateTime = date)
                    _effect.send(AddNewSlotEvent.DateTimeError)
                } else {
                    slot = slot.copy(beginDateTime = date)
                }
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        } else {
            slot = slot.copy(endDateTime = date)
            viewModelScope.launch {
                if (slot.beginDateTime > date) {
                    _effect.send(AddNewSlotEvent.DateTimeError)
                } else {
                    slot = slot.copy(endDateTime = date)
                }
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        }
    }

    fun updateTime(hour: Int, minutes: Int, isBegin: Boolean) {
        if (isBegin) {
            val updatedBeginTime = getUpdatedTime(hour, minutes, slot.beginDateTime)
            viewModelScope.launch {
                if (updatedBeginTime > slot.endDateTime) {
                    _effect.send(AddNewSlotEvent.DateTimeError)
                    slot = slot.copy(beginDateTime = updatedBeginTime, endDateTime = updatedBeginTime)
                } else {
                    slot = slot.copy(beginDateTime = updatedBeginTime)
                }
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        } else {
            val updatedEndTime = getUpdatedTime(hour, minutes, slot.endDateTime)
            viewModelScope.launch {
                if (updatedEndTime < slot.beginDateTime) {
                    _effect.send(AddNewSlotEvent.DateTimeError)
                    slot = slot.copy(endDateTime = slot.beginDateTime)
                } else {
                    slot = slot.copy(endDateTime = updatedEndTime)
                }
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
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
                    slot = slot.copy(
                        id = it.id,
                        room = it.room,
                        owner = it.owner,
                        comments = it.comments,
                        beginDateTime = it.beginDateTime,
                        endDateTime = it.beginDateTime
                    )
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(it))
                }
                .onFailure {
                    _effect.send(AddNewSlotEvent.AddNewSlotError("Печалька!")) // TODO (нужен отдельный эффект)
                    _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slot))
                }
        }
    }


    private fun saveSlot() {
        viewModelScope.launch(Dispatchers.IO) {
            if (checkSlot()) {
                slot = slot.copy(id = UUID.randomUUID().toString())
                val slotEntity = slot.toSlotsEntity()

                saveNewSlotUseCase.run(slotEntity)
                    .onFailure {
                        _effect.send(AddNewSlotEvent.SaveSlotError(it))
                    }
                    .onSuccess {
                        _effect.send(AddNewSlotEvent.SlotSavedSuccess)
                    }
            } else {
                // TODO (нужен отдельный эффект)
            }

        }
    }

    private suspend fun checkSlot(): Boolean {
        var isChecked = true
        if (slot.room.name.isEmpty()) {
            _effect.send(AddNewSlotEvent.RoomEmptyError)
            isChecked = false
        }
        if (slot.beginDateTime >= slot.endDateTime) {
            _effect.send(AddNewSlotEvent.DateTimeError)
            isChecked = false
        }
        return isChecked
    }


    private fun fetchDisplayState(room: Rooms?) {
        viewModelScope.launch {
            room?.let {
                slot = slot.copy(room = room)
                _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        }
    }

    private fun getUpdatedTime(hour: Int, minutes: Int, dateTime: Long): Long {
        val hourByMillis = hour * 3600000L
        val minutesByMillis = minutes * 60000L
        val currentStartDay = atStartOfDay(Date(dateTime))?.time ?: 0L
        return currentStartDay + hourByMillis + minutesByMillis
    }

    fun onRequestComment() {
        viewModelScope.launch {
            _effect.send(AddNewSlotEvent.OpenCommentDialog(slot.comments))
        }
    }

    fun onRoomSelectRequest() {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.Loading)
            withContext(Dispatchers.IO) {
                getAllRoomsUseCase.run(None)
            }
                .onSuccess {
                    _addNewSlotState.emit(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.ROOM, dataList = it))
                }
                .onFailure {
                    _effect.send(AddNewSlotEvent.GetRoomsError(it))
                }
        }
    }

    fun onUsersSelectRequest() {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.Loading)
            withContext(Dispatchers.IO) {
                getAllUsersUseCase.run(None)
            }
                .onSuccess {
                    _addNewSlotState.emit(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.USERS, dataList = it))
                }
                .onFailure {
                    _effect.send(AddNewSlotEvent.GetRoomsError(it))
                }
        }
    }

    fun updateRoom(room: Rooms) {
        viewModelScope.launch {
            slot = slot.copy(room = room)
            _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slot))
        }
    }

    fun updateUser(user: Users) {
        viewModelScope.launch {
            slot = slot.copy(owner = user)
            _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
        }
    }

    fun updateDisplayState() {
        viewModelScope.launch {
            _addNewSlotState.emit(AddNewSlotState.DisplaySlotState(slotRoom = slot))
        }
    }
}
