package com.testing.slotrooms.presentation.createroom

import com.testing.slotrooms.data.database.entities.RoomEntity

sealed class CreateRoomState {
    object Loading : CreateRoomState()
    object EmptyRoom : CreateRoomState()
    class RoomDisplayed(val room: RoomEntity?) : CreateRoomState()
}

sealed class CreateRoomEvent {
    class EntryScreen(val roomId: String?) : CreateRoomEvent()
}

sealed class CreateRoomEffect {
    class SaveError(val ex: Exception) : CreateRoomEffect()
    object SaveSuccess : CreateRoomEffect()
}