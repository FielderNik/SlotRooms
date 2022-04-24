package com.testing.slotrooms.presentation.createroom

import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.domain.usecases.CreateRoomUseCase
import com.testing.slotrooms.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase
) : BaseViewModel<CreateRoomEvent, CreateRoomState, CreateRoomEffect>(CreateRoomState.EmptyRoom) {

    private var room: RoomEntity? = null

    private fun updateRoom(roomId: String?) {
        if (roomId == null) {
            viewModelScope.launch {
                setState(CreateRoomState.RoomDisplayed(null))
            }
        } else {
            // TODO получить room по id и показать на скрине
        }
    }

    override fun handleEvent(event: CreateRoomEvent) {
        when (val currentState = state.value) {
            is CreateRoomState.EmptyRoom -> reduce(currentState, event)
            is CreateRoomState.RoomDisplayed -> {}
        }
    }

    private fun reduce(currentState: CreateRoomState.EmptyRoom, event: CreateRoomEvent) {
        when (event) {
            is CreateRoomEvent.EntryScreen -> {
                updateRoom(event.roomId)
            }
        }
    }

    fun saveRoom(roomName: String, capacity: Int, address: String, info: String) {
        room = room?.copy(
            name = roomName,
            capacity = capacity,
            address = address,
            info = info
        ) ?: RoomEntity(id = UUID.randomUUID().toString(), image = "", name = roomName, capacity = capacity, address = address, info = info)
        viewModelScope.launch {
            setState(CreateRoomState.Loading)
            withContext(Dispatchers.IO) {
                createRoomUseCase.run(CreateRoomUseCase.Params(
                    roomName = roomName,
                    capacity = capacity,
                    address = address,
                    info = info)
                )
            }
                .onFailure {
                    setState(CreateRoomState.RoomDisplayed(room))
                    sendEffect(CreateRoomEffect.SaveError(it))
                }
                .onSuccess {
                    setState(CreateRoomState.RoomDisplayed(room))
                    sendEffect(CreateRoomEffect.SaveSuccess)
                }
        }
    }

}