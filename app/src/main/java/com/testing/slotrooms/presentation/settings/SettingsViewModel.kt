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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _settingsScreenEffect: MutableStateFlow<SettingsScreenEffect?> = MutableStateFlow(null)
    val settingsScreenEffect: StateFlow<SettingsScreenEffect?> = _settingsScreenEffect


    override fun handleEvent(event: SettingsScreenEvent) {
        when (val currentState = _settingsScreenState.value) {
            is SettingsScreenState.DefaultState -> reduce(currentState, event)
            is SettingsScreenState.NewRoomDialogOpen -> reduce(currentState, event)
            is SettingsScreenState.NewUserDialogOpen -> reduce(currentState, event)
        }
    }

    private fun reduce(currentState: SettingsScreenState.DefaultState, event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.NewRoom.NewRoomClicked -> {
                viewModelScope.launch {
                    _settingsScreenState.emit(SettingsScreenState.NewRoomDialogOpen)
                }
            }
            is SettingsScreenEvent.NewUser.NewUserClicked -> {
                viewModelScope.launch {
                    _settingsScreenState.emit(SettingsScreenState.NewUserDialogOpen)
                }
            }
            SettingsScreenEvent.EmptySlotClicked -> {
            }
            SettingsScreenEvent.OpenDefaultScreen -> {
            }
        }
    }

    private fun reduce(currentState: SettingsScreenState.NewRoomDialogOpen, event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.OpenDefaultScreen -> {
                produceDefaultState()
            }
            SettingsScreenEvent.EmptySlotClicked -> {
            }
            SettingsScreenEvent.NewRoom.NewRoomClicked -> {
            }
            is SettingsScreenEvent.NewRoom.NewRoomConfirmed -> {
                saveNewRoom(event.roomName)
            }
            SettingsScreenEvent.NewUser.NewUserClicked -> {
            }
            is SettingsScreenEvent.NewUser.NewUserConfirmed -> {
            }
        }
    }

    private fun reduce(currentState: SettingsScreenState.NewUserDialogOpen, event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.OpenDefaultScreen -> {
                produceDefaultState()
            }
            SettingsScreenEvent.EmptySlotClicked -> {
            }
            SettingsScreenEvent.NewRoom.NewRoomClicked -> {
            }
            is SettingsScreenEvent.NewRoom.NewRoomConfirmed -> {
            }
            SettingsScreenEvent.NewUser.NewUserClicked -> {
            }
            is SettingsScreenEvent.NewUser.NewUserConfirmed -> {
                saveNewUser(event.userName)
            }
        }
    }

    private fun produceDefaultState() {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.DefaultState)
        }
    }

    private fun saveNewRoom(roomName: String) {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.Loading)
            withContext(Dispatchers.IO) {
                addNewRoomUseCase.run(roomName)
            }
                .onSuccess {
                    _settingsScreenEffect.emit(SettingsScreenEffect.SettingsScreenSuccess.NewRoomSaveSuccess)
                }
                .onFailure {
                    _settingsScreenEffect.emit(SettingsScreenEffect.SettingsScreenError.NewRoomError(it))
                }
            _settingsScreenState.emit(SettingsScreenState.DefaultState)

        }
    }

    private fun saveNewUser(userName: String) {
        viewModelScope.launch {
            _settingsScreenState.emit(SettingsScreenState.Loading)
            withContext(Dispatchers.IO) {
                addNewUserUseCase.run(userName)
            }
                .onSuccess {
                    _settingsScreenEffect.emit(SettingsScreenEffect.SettingsScreenSuccess.NewUserSaveSuccess)
                }
                .onFailure {
                    _settingsScreenEffect.emit(SettingsScreenEffect.SettingsScreenError.NewUserError(it))
                }
            _settingsScreenState.emit(SettingsScreenState.DefaultState)

        }
    }

    suspend fun resetErrorStatus() {
        _settingsScreenEffect.emit(null)
    }


}