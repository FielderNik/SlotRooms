package com.testing.slotrooms.presentation.filter

import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.presentation.model.SlotFilter

sealed class SlotFilterState {
    object EmptyFilterState : SlotFilterState()
    object FilterLoading : SlotFilterState()
    data class ResultFilterState(val slotFilter: SlotFilter?) : SlotFilterState()

    sealed class ChoiceDialogState : SlotFilterState() {
        class RoomDialogOpened(val rooms: List<RoomEntity>) : ChoiceDialogState()
        class UserDialogOpened(val users: List<UserEntity>) : ChoiceDialogState()
    }
}

sealed class SlotFilterEvent {
    class RangeDatePickedEvent(val beginDate: Long, val endDate: Long) : SlotFilterEvent()
    object RangeDateCanceledEvent : SlotFilterEvent()
    object RoomDialogOpen : SlotFilterEvent()
    object UserDialogOpen : SlotFilterEvent()

    sealed class RoomDialogEvent : SlotFilterEvent() {
        object RoomDialogCanceled : RoomDialogEvent()
        class RoomDialogConfirmed(val room: RoomEntity) : RoomDialogEvent()
    }

    sealed class UserDialogEvent : SlotFilterEvent() {
        object UserDialogCanceled : UserDialogEvent()
        class UserDialogConfirmed(val user: UserEntity) : UserDialogEvent()
    }
}

sealed class SlotFilterEffect {

}