package com.testing.slotrooms.presentation.slots

import com.testing.slotrooms.presentation.model.SlotRoom

sealed class SlotsScreenState {
    object SlotsLoading : SlotsScreenState()
    data class SlotsSuccess(val slots: List<SlotRoom>) : SlotsScreenState()
    object SlotsEmptyScreen : SlotsScreenState()
}

sealed class SlotsScreenEvent {
    object SlotsEnterScreenEvent : SlotsScreenEvent()
}

sealed class SlotsScreenEffect {
    data class ErrorLoading(val exception: Exception) : SlotsScreenEffect()
}