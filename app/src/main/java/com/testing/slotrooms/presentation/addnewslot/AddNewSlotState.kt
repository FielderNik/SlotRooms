package com.testing.slotrooms.presentation.addnewslot

import com.testing.slotrooms.presentation.model.SlotRoom

sealed class AddNewSlotState {
    object Loading : AddNewSlotState()
    data class OpenSlotDialog(val dialogType: DialogType, val dataList: List<Any>) : AddNewSlotState()
    data class DisplaySlotState(val slotRoom: SlotRoom) : AddNewSlotState()
}

sealed interface UI
sealed class AddNewSlotEvent {
    data class EnterScreen(val slotRoomId: String? = null) : AddNewSlotEvent(), UI
    data class CommentSubmittedEvent(val comment: String) : AddNewSlotEvent(), UI
    object SaveSlotEvent : AddNewSlotEvent()
    object CancelSlotEvent : AddNewSlotEvent()

}

sealed class AddNewSlotEffect {
    data class AddNewSlotError(val message: String) : AddNewSlotEffect()
    object DateTimeError : AddNewSlotEffect()
    object RoomEmptyError : AddNewSlotEffect()
    object OwnerEmptyError : AddNewSlotEffect()
    object SlotSavedSuccess : AddNewSlotEffect()
    data class GetRoomsError(val exception: Exception) : AddNewSlotEffect()
    data class GetUsersError(val exception: Exception) : AddNewSlotEffect()
    data class SaveSlotError(val exception: Exception) : AddNewSlotEffect()
    data class OpenCommentDialog(val comment: String) : AddNewSlotEffect()

}


data class SlotDialog(
    val dialogType: DialogType = DialogType.ROOM,
    val isOpen: Boolean = false,
)

enum class DialogType {
    ROOM,
    USERS
}
