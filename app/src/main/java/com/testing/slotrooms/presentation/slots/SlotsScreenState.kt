package com.testing.slotrooms.presentation.slots

import com.testing.slotrooms.presentation.model.SlotRoom

sealed class SlotsScreenState {
    object SlotsLoading : SlotsScreenState()
    data class SlotsSuccess(val slots: List<SlotRoom>) : SlotsScreenState()
    object SlotsEmptyScreen : SlotsScreenState()
    object FilterOpened : SlotsScreenState()
}

sealed class SlotsScreenEvent {
    object SlotsEnterScreenEvent : SlotsScreenEvent()

    sealed class SlotFilterEvent : SlotsScreenEvent() {
        object OpenFilterEvent : SlotFilterEvent()
        object CancelFilterEvent : SlotFilterEvent()
        object SaveFilterEvent : SlotFilterEvent()
    }
}

sealed class SlotsScreenEffect {
    data class ErrorLoading(val exception: Exception) : SlotsScreenEffect()
}