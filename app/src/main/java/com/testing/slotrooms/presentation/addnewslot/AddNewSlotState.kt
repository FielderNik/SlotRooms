package com.testing.slotrooms.presentation.addnewslot

import com.testing.slotrooms.presentation.model.SlotRoom

sealed class AddNewSlotState {
    object Loading : AddNewSlotState()
    data class OpenSlotDialog(val dialogType: DialogType, val dataList: List<Any>) : AddNewSlotState()
    data class DisplaySlotState(val slotRoom: SlotRoom) : AddNewSlotState()
}

sealed interface UI
sealed interface Effects
sealed class AddNewSlotEvent {
    data class EnterScreen(val slotRoomId: String? = null) : AddNewSlotEvent(), UI
    data class CommentSubmittedEvent(val comment: String) : AddNewSlotEvent(), UI
    object SaveSlotEvent : AddNewSlotEvent()
    object CancelSlotEvent : AddNewSlotEvent()


    data class AddNewSlotError(val message: String) : AddNewSlotEvent(), Effects
    object DateTimeError : AddNewSlotEvent(), Effects
    object RoomEmptyError : AddNewSlotEvent(), Effects
    object OwnerEmptyError : AddNewSlotEvent(), Effects
    object SlotSavedSuccess : AddNewSlotEvent(), Effects
    data class GetRoomsError(val exception: Exception) : Effects
    data class GetUsersError(val exception: Exception) : Effects
    data class SaveSlotError(val exception: Exception) : Effects
    data class OpenCommentDialog(val comment: String) : Effects

}

sealed class AddNewSlotEffect {
    class ShowError(val exception: Exception) : AddNewSlotEffect()

}


data class SlotDialog(
    val dialogType: DialogType = DialogType.ROOM,
    val isOpen: Boolean = false,
)

enum class DialogType {
    ROOM,
    USERS
}
