package com.testing.slotrooms.presentation.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.slots.*
import com.testing.slotrooms.domain.usecases.GetAllSlotsUseCase
import com.testing.slotrooms.model.database.entities.Users
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val getAllSlotsUseCase: GetAllSlotsUseCase,
) : ViewModel(), EventHandler<SlotsScreenEvent> {

    private val _slotsScreenState: MutableStateFlow<SlotsScreenState> = MutableStateFlow(SlotsScreenState.SlotsEmptyScreen)
    val slotsScreenState: StateFlow<SlotsScreenState> = _slotsScreenState

    private val _slotsScreenEffect: MutableStateFlow<SlotsScreenEffect?> = MutableStateFlow(null)
    val slotsScreenEffect: StateFlow<SlotsScreenEffect?> = _slotsScreenEffect

    override fun handleEvent(event: SlotsScreenEvent) {
        when (val currentState = _slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> reduce(currentState, event)
            is SlotsScreenState.SlotsLoading -> reduce(currentState, event)
            is SlotsScreenState.SlotsSuccess -> reduce(currentState, event)
        }
    }


    private fun reduce(currentState: SlotsScreenState.SlotsEmptyScreen, event: SlotsScreenEvent) {
        when(event) {
            SlotsScreenEvent.SlotsEnterScreenEvent -> updateSlotScreen()
        }

    }

    private fun reduce(currentState: SlotsScreenState.SlotsLoading, event: SlotsScreenEvent) {

    }

    private fun reduce(currentState: SlotsScreenState.SlotsSuccess, event: SlotsScreenEvent) {
        when(event) {
            SlotsScreenEvent.SlotsEnterScreenEvent -> updateSlotScreen()
        }
    }

    private fun updateSlotScreen() {
        viewModelScope.launch {
            _slotsScreenState.emit(SlotsScreenState.SlotsLoading)
            delay(10_000L)
            withContext(Dispatchers.IO) {
                getAllSlotsUseCase.run(None)
            }
                .onFailure {
                    viewModelScope.launch {
                        _slotsScreenEffect.emit(SlotsScreenEffect.ErrorLoading(it))
                    }
                }
                .onSuccess {
                    viewModelScope.launch {
                        _slotsScreenState.emit(SlotsScreenState.SlotsSuccess(it))
                    }
                }
        }
    }

}