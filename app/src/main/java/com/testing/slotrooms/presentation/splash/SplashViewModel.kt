package com.testing.slotrooms.presentation.splash

import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.LoadUsersRoomsSlotsUseCase
import com.testing.slotrooms.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadUsersRoomsSlotsUseCase: LoadUsersRoomsSlotsUseCase,
) : BaseViewModel<SplashEvent, SplashState, SplashEffect>(SplashState.Loading) {

    suspend fun initialization() {
        viewModelScope.launch {
            loadUsersRoomsSlots()
        }
    }

    override fun handleEvent(event: SplashEvent) {
        super.handleEvent(event)
    }

    private suspend fun loadUsersRoomsSlots() {
        withContext(Dispatchers.IO) {
            loadUsersRoomsSlotsUseCase.run(None)
        }
            .onFailure {
                handleException(it)
            }
            .onSuccess {
                setState(SplashState.Completed)
                sendEffect(SplashEffect.LoadCompleted)
            }
    }

    private suspend fun handleException(exception: Exception) {
        when(exception) {
            is SlotsException.RemoteException.ServerUnavailable -> {
                setState(SplashState.ServerUnavailable(exception))
            }
            else -> {
                setState(SplashState.Completed)
                sendEffect(SplashEffect.Failed.UsualFailed(exception))
            }
        }
    }

}