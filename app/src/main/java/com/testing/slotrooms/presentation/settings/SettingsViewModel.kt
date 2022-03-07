package com.testing.slotrooms.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.core.EventHandler
import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.AddNewRoomUseCase
import com.testing.slotrooms.domain.usecases.AddNewUserUseCase
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
class SettingsViewModel @Inject constructor(
    private val addNewRoomUseCase: AddNewRoomUseCase,
    private val addNewUserUseCase: AddNewUserUseCase
) : ViewModel(), EventHandler<SettingsScreenEvent> {

    private val _settingsScreenState: MutableStateFlow<SettingsScreenState> = MutableStateFlow(SettingsScreenState.DefaultState)
    val settingsScreenState: StateFlow<SettingsScreenState> = _settingsScreenState

    private val _settingsScreenEffect = Channel<SettingsScreenEffect>()
    val settingsScreenEffect = _settingsScreenEffect.receiveAsFlow()


    override fun handleEvent(event: SettingsScreenEvent) {
        when (val currentState = _settingsScreenState.value) {
            is SettingsScreenState.DefaultState -> reduce(currentState, event)
            SettingsScreenState.Loading -> {}
        }
    }

    private fun reduce(currentState: SettingsScreenState.DefaultState, event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.NewRoom.NewRoomClicked -> {
                viewModelScope.launch {
                    _settingsScreenEffect.send(SettingsScreenEffect.OpenRoomDialog)
                }
            }
            is SettingsScreenEvent.NewUser.NewUserClicked -> {
                viewModelScope.launch {
                    _settingsScreenEffect.send(SettingsScreenEffect.OpenUserDialog)
                }
            }
            SettingsScreenEvent.EmptySlotClicked -> {
            }
            SettingsScreenEvent.OpenDefaultScreen -> {
            }
        }
    }


    private fun produceDefaultState() {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.DefaultState)
        }
    }

    fun saveNewRoom(roomName: String) {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.Loading)
            withContext(Dispatchers.IO) {
                addNewRoomUseCase.run(roomName)
            }
                .onSuccess {
                    _settingsScreenEffect.send(SettingsScreenEffect.SettingsScreenSuccess.NewRoomSaveSuccess)
                }
                .onFailure {
                    _settingsScreenEffect.send(SettingsScreenEffect.SettingsScreenError.NewRoomError(it))
                }
            _settingsScreenState.emit(SettingsScreenState.DefaultState)

        }
    }

    fun saveNewUser(userName: String) {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.Loading)
            withContext(Dispatchers.IO) {
                addNewUserUseCase.run(userName)
            }
                .onSuccess {
                    _settingsScreenEffect.send(SettingsScreenEffect.SettingsScreenSuccess.NewUserSaveSuccess)
                }
                .onFailure {
                    _settingsScreenEffect.send(SettingsScreenEffect.SettingsScreenError.NewUserError(it))
                }
            _settingsScreenState.emit(SettingsScreenState.DefaultState)

        }
    }

}