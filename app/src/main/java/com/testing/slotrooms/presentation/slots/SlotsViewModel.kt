package com.testing.slotrooms.presentation.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.GetAllSlotsUseCase
import com.testing.slotrooms.presentation.model.SlotFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val getAllSlotsUseCase: GetAllSlotsUseCase,
) : ViewModel(), EventHandler<SlotsScreenEvent> {

    private val _slotsScreenState: MutableStateFlow<SlotsScreenState> = MutableStateFlow(SlotsScreenState.SlotsEmptyScreen)
    val slotsScreenState: StateFlow<SlotsScreenState> = _slotsScreenState

    private val _slotsScreenEffect: MutableStateFlow<SlotsScreenEffect?> = MutableStateFlow(null)
    val slotsScreenEffect: StateFlow<SlotsScreenEffect?> = _slotsScreenEffect // TODO переписать на Channel

    private val _effectChannel = Channel<SlotsScreenEffect>()
    val effectChannel = _effectChannel.receiveAsFlow()

    override fun handleEvent(event: SlotsScreenEvent) {
        when (val currentState = _slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> reduce(currentState, event)
            is SlotsScreenState.SlotsLoading -> reduce(currentState, event)
            is SlotsScreenState.SlotsSuccess -> reduce(currentState, event)
            is SlotsScreenState.FilterOpened -> reduce(currentState, event)
        }
    }

    private fun reduce(currentState: SlotsScreenState.FilterOpened, event: SlotsScreenEvent) {
        when (event) {
            is SlotsScreenEvent.SlotFilterEvent -> {
                when (event) {
                    SlotsScreenEvent.SlotFilterEvent.CancelFilterEvent -> {
                        updateSlotScreen(null)
                    }
                    SlotsScreenEvent.SlotFilterEvent.OpenFilterEvent -> {}
                    SlotsScreenEvent.SlotFilterEvent.SaveFilterEvent -> {}
                }
            }
        }
    }


    private fun reduce(currentState: SlotsScreenState.SlotsEmptyScreen, event: SlotsScreenEvent) {
        when (event) {
            is SlotsScreenEvent.SlotsEnterScreenEvent -> updateSlotScreen(event.filter)
        }

    }

    private fun reduce(currentState: SlotsScreenState.SlotsLoading, event: SlotsScreenEvent) {

    }

    private fun reduce(currentState: SlotsScreenState.SlotsSuccess, event: SlotsScreenEvent) {
        when (event) {
            is SlotsScreenEvent.SlotsEnterScreenEvent -> updateSlotScreen(event.filter)
            SlotsScreenEvent.SlotFilterEvent.OpenFilterEvent -> {
                viewModelScope.launch {
                    _slotsScreenState.emit(SlotsScreenState.FilterOpened)
                }
            }
        }
    }

    private fun updateSlotScreen(filter: SlotFilter?) {
        viewModelScope.launch {
            _slotsScreenState.emit(SlotsScreenState.SlotsLoading)
            withContext(Dispatchers.IO) {
                getAllSlotsUseCase.run(GetAllSlotsUseCase.Params(filter = filter))
            }
                .onFailure {
                    _slotsScreenEffect.emit(SlotsScreenEffect.ErrorLoading(it))
                    it.printStackTrace()
                }
                .onSuccess {
                    _slotsScreenState.emit(SlotsScreenState.SlotsSuccess(it))
                }
        }
    }

    suspend fun resetErrorStatus() {
        _slotsScreenEffect.emit(null)
    }

}