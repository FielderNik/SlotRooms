package com.testing.slotrooms.presentation.slots

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.DeleteSlotUseCase
import com.testing.slotrooms.domain.usecases.GetAllSlotsUseCase
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val getAllSlotsUseCase: GetAllSlotsUseCase,
    private val deleteSlotUseCase: DeleteSlotUseCase,
) : ViewModel(), EventHandler<SlotsScreenEvent> {

    private val _slotsScreenState: MutableStateFlow<SlotsScreenState> = MutableStateFlow(SlotsScreenState.SlotsEmptyScreen)
    val slotsScreenState: StateFlow<SlotsScreenState> = _slotsScreenState

//    private val _slotsScreenEffect: MutableStateFlow<SlotsScreenEffect?> = MutableStateFlow(null)
//    val slotsScreenEffect: StateFlow<SlotsScreenEffect?> = _slotsScreenEffect // TODO переписать на Channel

//    private val _effectChannel = Channel<SlotsScreenEffect>()
//    val effectChannel = _effectChannel.receiveAsFlow()

    private val _effectChanel = MutableSharedFlow<SlotsScreenEffect>()
    val effectChannel = _effectChanel.asSharedFlow()

    override fun handleEvent(event: SlotsScreenEvent) {
        Log.d("milk", "SlotsViewModel: handleEvent run")
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
                    _effectChanel.emit(SlotsScreenEffect.ErrorLoading(it))
                    it.printStackTrace()
                }
                .onSuccess {
                    _slotsScreenState.emit(SlotsScreenState.SlotsSuccess(it))
                }
        }
    }

    fun onFilterCleared(filter: SlotFilter?) {
        updateSlotScreen(filter)
    }

    fun deleteSlot(slot: SlotRoom, filter: SlotFilter?) {
        viewModelScope.launch {
            _slotsScreenState.emit(SlotsScreenState.SlotsLoading)
            withContext(Dispatchers.IO) {
                deleteSlotUseCase.run(slot)
            }
                .onSuccess {
                    updateSlotScreen(filter)
                }
                .onFailure {
                    _effectChanel.emit(SlotsScreenEffect.ErrorLoading(it))
                }
        }

    }

//    suspend fun resetErrorStatus() {
//        _effectChanel.emit(null)
//    }

}