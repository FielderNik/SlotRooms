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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    override fun handleEvent(event: SlotsScreenEvent) {
        when (val currentState = _slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> reduce(currentState, event)
            is SlotsScreenState.SlotsLoading -> reduce(currentState, event)
            is SlotsScreenState.SlotsSuccess -> reduce(currentState, event)
        }
    }


    private fun reduce(currentState: SlotsScreenState.SlotsEmptyScreen, event: SlotsScreenEvent) {
        viewModelScope.launch {
            _slotsScreenState.emit(SlotsScreenState.SlotsLoading)
            withContext(Dispatchers.IO) {
                getAllSlotsUseCase.run(None)
            }
                .onFailure {

                }
                .onSuccess {
                    viewModelScope.launch {
                        _slotsScreenState.emit(SlotsScreenState.SlotsSuccess(it))
                    }
                }
        }
    }

    private fun reduce(currentState: SlotsScreenState.SlotsLoading, event: SlotsScreenEvent) {

    }

    private fun reduce(currentState: SlotsScreenState.SlotsSuccess, event: SlotsScreenEvent) {

    }


    val slots = listOf<SlotsModel>(
        SlotsModel(
            roomName = "Room 1",
            startMeeting = Date(1640289600 * 1000L),
            finishMeeting = Date(1640275200 * 1000L),
            owner = userVasya,
            members = listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
    )
}