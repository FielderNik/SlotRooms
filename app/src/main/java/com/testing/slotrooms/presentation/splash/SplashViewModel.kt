package com.testing.slotrooms.presentation.splash

import com.testing.slotrooms.core.None
import com.testing.slotrooms.domain.usecases.GetAllRoomsUseCase
import com.testing.slotrooms.domain.usecases.GetAllSlotsUseCase
import com.testing.slotrooms.domain.usecases.GetAllUsersUseCase
import com.testing.slotrooms.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getAllSlotsUseCase: GetAllSlotsUseCase,
): BaseViewModel<SplashEvent, SplashState, SplashEffect>(SplashState.Loading) {

    suspend fun initialization() {
        loadUsers()
        loadRooms()
        loadSlots()
        setState(SplashState.Completed)
        sendEffect(SplashEffect.LoadCompleted)
    }

    private suspend fun loadUsers() {
        getAllUsersUseCase.run(None)
    }

    private suspend fun loadRooms() {
        getAllRoomsUseCase.run(None)
    }

    private suspend fun loadSlots() {
        getAllSlotsUseCase.run(GetAllSlotsUseCase.Params(null))
    }
}