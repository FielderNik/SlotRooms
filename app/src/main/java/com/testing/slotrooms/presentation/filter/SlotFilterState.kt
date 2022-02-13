package com.testing.slotrooms.presentation.filter

import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.presentation.model.SlotFilter

sealed class SlotFilterState {
    object EmptyFilterState : SlotFilterState()
    object FilterLoading : SlotFilterState()
    data class ResultFilterState(val slotFilter: SlotFilter?) : SlotFilterState()

    sealed class ChoiceDialogState : SlotFilterState() {
        class RoomDialogOpened(val rooms: List<Rooms>) : ChoiceDialogState()
        class UserDialogOpened(val users: List<Users>) : ChoiceDialogState()
    }
}

sealed class SlotFilterEvent {
    class RangeDatePickedEvent(val beginDate: Long, val endDate: Long) : SlotFilterEvent()
    object RangeDateCanceledEvent : SlotFilterEvent()
    object RoomDialogOpen : SlotFilterEvent()
    object UserDialogOpen : SlotFilterEvent()

    sealed class RoomDialogEvent : SlotFilterEvent() {
        object RoomDialogCanceled : RoomDialogEvent()
        class RoomDialogConfirmed(val room: Rooms) : RoomDialogEvent()
    }

    sealed class UserDialogEvent : SlotFilterEvent() {
        object UserDialogCanceled : UserDialogEvent()
        class UserDialogConfirmed(val user: Users) : UserDialogEvent()
    }
}

sealed class SlotFilterEffect {

}