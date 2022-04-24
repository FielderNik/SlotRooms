package com.testing.slotrooms.presentation.addnewslot

import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.domain.usecases.*
import com.testing.slotrooms.presentation.BaseViewModel
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.utils.atStartOfDay
import com.testing.slotrooms.utils.toSlotsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val createSlotUseCase: CreateSlotUseCase,
    private val getSlotRoomByIdUseCase: GetSlotRoomByIdUseCase,
) : BaseViewModel<AddNewSlotEvent, AddNewSlotState, AddNewSlotEffect>(AddNewSlotState.Loading), EventHandler<AddNewSlotEvent> {

    private val currentDate = SlotRoom.getCurrentDate()
    private var slot = SlotRoom(beginDateTime = currentDate, endDateTime = currentDate)

//    private val _effect = Channel<Effects>()
//    val effect = _effect.receiveAsFlow()

//    private val _effect = MutableSharedFlow<Effects>(extraBufferCapacity = 1)
//    val effect = _effect.asSharedFlow()

//    private val _effect = MutableStateFlow<Effects?>(null)
//    val effect = _effect.asStateFlow()
//
//    private val _addNewSlotState: MutableStateFlow<AddNewSlotState> = MutableStateFlow(AddNewSlotState.DisplaySlotState(slot))
//    val addNewSlotState: StateFlow<AddNewSlotState> = _addNewSlotState

    override fun handleEvent(event: AddNewSlotEvent) {
        when (val currentState = state.value) {
            is AddNewSlotState.DisplaySlotState -> reduce(event, currentState)
            is AddNewSlotState.Loading -> reduce(event, currentState)
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
                fetchEmptyState(event.slotRoomId)
            }
        }
    }

    private fun reduce(event: AddNewSlotEvent, currentState: AddNewSlotState.Loading) {
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
                fetchEmptyState(event.slotRoomId)
            }
        }
    }


    fun updateComment(comment: String) {
        slot = slot.copy(comments = comment)
        viewModelScope.launch {
            setState(AddNewSlotState.DisplaySlotState(slot))
        }
    }

    fun updateDate(date: Long, isBegin: Boolean) {
        if (isBegin) {
            slot = slot.copy(beginDateTime = date)
            viewModelScope.launch {
                if (date > slot.endDateTime) {
                    slot = slot.copy(beginDateTime = date, endDateTime = date)
                    sendEffect(AddNewSlotEffect.DateTimeError)
                } else {
                    slot = slot.copy(beginDateTime = date)
                }
                setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        } else {
            slot = slot.copy(endDateTime = date)
            viewModelScope.launch {
                if (slot.beginDateTime > date) {
                    sendEffect(AddNewSlotEffect.DateTimeError)
                } else {
                    slot = slot.copy(endDateTime = date)
                }
                setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        }
    }

    fun updateTime(hour: Int, minutes: Int, isBegin: Boolean) {
        if (isBegin) {
            val updatedBeginTime = getUpdatedTime(hour, minutes, slot.beginDateTime)
            viewModelScope.launch {
                if (updatedBeginTime > slot.endDateTime) {
                    sendEffect(AddNewSlotEffect.DateTimeError)
                    slot = slot.copy(beginDateTime = updatedBeginTime, endDateTime = updatedBeginTime)
                } else {
                    slot = slot.copy(beginDateTime = updatedBeginTime)
                }
                setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        } else {
            val updatedEndTime = getUpdatedTime(hour, minutes, slot.endDateTime)
            viewModelScope.launch {
                if (updatedEndTime < slot.beginDateTime) {
                    sendEffect(AddNewSlotEffect.DateTimeError)
                    slot = slot.copy(endDateTime = slot.beginDateTime)
                } else {
                    slot = slot.copy(endDateTime = updatedEndTime)
                }
                setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
            }
        }
    }

    private fun fetchEmptyState(slotRoomId: String?) {
        if (slotRoomId == null) {
            viewModelScope.launch {
                setState(AddNewSlotState.DisplaySlotState(slot))
            }
        } else {
            viewModelScope.launch {
                setState(AddNewSlotState.Loading)
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
                        setState(AddNewSlotState.DisplaySlotState(it))
                    }
                    .onFailure {
                        sendEffect(AddNewSlotEffect.AddNewSlotError("Печалька!")) // TODO (нужен отдельный эффект)
                        setState(AddNewSlotState.DisplaySlotState(slot))
                    }
            }
        }

    }


    private fun saveSlot() {
        viewModelScope.launch {
            if (checkSlot()) {
                slot = slot.copy(id = UUID.randomUUID().toString())
                val slotEntity = slot.toSlotsEntity()
                withContext(Dispatchers.IO) {
                    createSlotUseCase.run(slotEntity)
                }
                    .onFailure {
                        setState(AddNewSlotState.DisplaySlotState(slot))
                        sendEffect(AddNewSlotEffect.SaveSlotError(it))
                    }
                    .onSuccess {
                        sendEffect(AddNewSlotEffect.SlotSavedSuccess)
                    }
            } else {
                // TODO (нужен отдельный эффект)
            }

        }
    }

    private suspend fun checkSlot(): Boolean {
        if (slot.room.name.isEmpty()) {
            sendEffect(AddNewSlotEffect.RoomEmptyError)
            return false
        }
        if (slot.beginDateTime >= slot.endDateTime) {
            sendEffect(AddNewSlotEffect.DateTimeError)
            return false
        }
        return true
    }


    private fun fetchDisplayState(room: RoomEntity?) {
        viewModelScope.launch {
            room?.let {
                slot = slot.copy(room = room)
                setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
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
            sendEffect(AddNewSlotEffect.OpenCommentDialog(slot.comments))
        }
    }

    fun onRoomSelectRequest() {
        viewModelScope.launch {
            setState(AddNewSlotState.Loading)
            withContext(Dispatchers.IO) {
                getAllRoomsUseCase.run(None)
            }
                .onSuccess {
                    setState(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.ROOM, dataList = it))
                }
                .onFailure {
                    sendEffect(AddNewSlotEffect.GetRoomsError(it))
                    setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
                }
        }
    }

    fun onUsersSelectRequest() {
        viewModelScope.launch {
            setState(AddNewSlotState.Loading)
            withContext(Dispatchers.IO) {
                getAllUsersUseCase.run(None)
            }
                .onSuccess {
                    setState(AddNewSlotState.OpenSlotDialog(dialogType = DialogType.USERS, dataList = it))
                }
                .onFailure {
                    sendEffect(AddNewSlotEffect.GetRoomsError(it))
                    setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
                }
        }
    }

    fun updateRoom(room: RoomEntity) {
        viewModelScope.launch {
            slot = slot.copy(room = room)
            setState(AddNewSlotState.DisplaySlotState(slot))
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            slot = slot.copy(owner = user)
            setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
        }
    }

    fun updateDisplayState() {
        viewModelScope.launch {
            setState(AddNewSlotState.DisplaySlotState(slotRoom = slot))
        }
    }

//    fun clearState() {
//        viewModelScope.launch {
//            sendEffect(null)
//        }
//    }
}
