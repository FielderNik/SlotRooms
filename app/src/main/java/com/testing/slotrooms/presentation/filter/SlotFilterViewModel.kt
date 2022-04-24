package com.testing.slotrooms.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.GetAllRoomsUseCase
import com.testing.slotrooms.domain.usecases.GetAllUsersUseCase
import com.testing.slotrooms.presentation.model.SlotFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SlotFilterViewModel @Inject constructor(
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
//) : BaseViewModel<SlotFilterEvent>() {
) : ViewModel() {

    private var slotFilter: SlotFilter = SlotFilter()

    private val _slotFilterState: MutableStateFlow<SlotFilterState> = MutableStateFlow(SlotFilterState.ResultFilterState(slotFilter))
    val slotFilterState: StateFlow<SlotFilterState> = _slotFilterState


    /*override*/ fun handleEvent(event: SlotFilterEvent) {
        when (val currentState = _slotFilterState.value) {
            is SlotFilterState.EmptyFilterState -> reduce(currentState, event)
            is SlotFilterState.FilterLoading -> reduce(currentState, event)
            is SlotFilterState.ResultFilterState -> reduce(currentState, event)
            is SlotFilterState.ChoiceDialogState.RoomDialogOpened -> reduce(currentState, event)
            is SlotFilterState.ChoiceDialogState.UserDialogOpened -> reduce(currentState, event)
        }
    }

    private fun reduce(currentState: SlotFilterState.ChoiceDialogState.UserDialogOpened, event: SlotFilterEvent) {
        when (event) {
            is SlotFilterEvent.UserDialogEvent -> {
                when (event) {
                    is SlotFilterEvent.UserDialogEvent.UserDialogCanceled -> {
                        updateResultState()
                    }
                    is SlotFilterEvent.UserDialogEvent.UserDialogConfirmed -> {
//                        slotFilter = slotFilter.copy(owner = event.user)
                        slotFilter.owner = event.user
                        updateResultState()
                    }

                }
            }
        }
    }

    private fun reduce(currentState: SlotFilterState.ChoiceDialogState.RoomDialogOpened, event: SlotFilterEvent) {
        when (event) {
            is SlotFilterEvent.RoomDialogEvent -> {
                when (event) {
                    is SlotFilterEvent.RoomDialogEvent.RoomDialogCanceled -> {
                        updateResultState()
                    }
                    is SlotFilterEvent.RoomDialogEvent.RoomDialogConfirmed -> {
//                        slotFilter = slotFilter.copy(room = event.room)
                        slotFilter.room = event.room
                        updateResultState()
                    }

                }
            }
        }
    }

    private fun reduce(currentState: SlotFilterState.ResultFilterState, event: SlotFilterEvent) {
        when (event) {
            is SlotFilterEvent.RangeDatePickedEvent -> {
//                slotFilter = slotFilter.copy(beginDate = event.beginDate, endDate = event.endDate)
                slotFilter.beginDate = event.beginDate
                slotFilter.endDate = event.endDate
                updateResultState()
            }
            SlotFilterEvent.RangeDateCanceledEvent -> updateResultState()
            SlotFilterEvent.RoomDialogEvent.RoomDialogCanceled -> {}
            is SlotFilterEvent.RoomDialogEvent.RoomDialogConfirmed -> {}

            SlotFilterEvent.RoomDialogOpen -> {
                getRoomsAndUpdateStates()
            }

            SlotFilterEvent.UserDialogOpen -> {
                getUsersAndUpdateStates()
            }
        }
    }


    private fun reduce(currentState: SlotFilterState.FilterLoading, event: SlotFilterEvent) {

    }

    private fun reduce(currentState: SlotFilterState.EmptyFilterState, event: SlotFilterEvent) {

    }

    private fun getRoomsAndUpdateStates() {
        viewModelScope.launch {
            _slotFilterState.emit(SlotFilterState.FilterLoading)
            withContext(Dispatchers.IO) {
                getAllRoomsUseCase.run(None)
            }
                .onFailure {
                    //TODO показать ошибку
                }
                .onSuccess {
                    _slotFilterState.emit(SlotFilterState.ChoiceDialogState.RoomDialogOpened(it))
                }
        }
    }

    private fun getUsersAndUpdateStates() {
        viewModelScope.launch {
            _slotFilterState.emit(SlotFilterState.FilterLoading)
            withContext(Dispatchers.IO) {
                getAllUsersUseCase.run(None)
            }
                .onFailure {
                    //TODO показать ошибку
                }
                .onSuccess {
                    _slotFilterState.emit(SlotFilterState.ChoiceDialogState.UserDialogOpened(it))
                }
        }
    }

    fun updateSlotScreen() {
        getRoomsAndUpdateStates()
    }

    private fun updateState(state: SlotFilterState) {
        viewModelScope.launch {
            _slotFilterState.emit(state)
        }
    }

    private fun updateResultState() {
        viewModelScope.launch {
            _slotFilterState.emit(SlotFilterState.ResultFilterState(slotFilter))
        }
    }


}