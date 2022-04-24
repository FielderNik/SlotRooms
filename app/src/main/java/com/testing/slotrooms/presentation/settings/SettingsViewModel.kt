package com.testing.slotrooms.presentation.settings

import com.testing.slotrooms.core.onFailure
import com.testing.slotrooms.core.onSuccess
import com.testing.slotrooms.domain.usecases.CreateRoomUseCase
import com.testing.slotrooms.domain.usecases.CreateUserUseCase
import com.testing.slotrooms.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val createRoomUseCase: CreateRoomUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel<SettingsScreenEvent, SettingsScreenState, SettingsScreenEffect>(SettingsScreenState.DefaultState)/*, EventHandler<SettingsScreenEvent> */ {

    override fun handleEvent(event: SettingsScreenEvent) {
        when (val currentState = state.value) {
            is SettingsScreenState.DefaultState -> reduce(currentState, event)
            SettingsScreenState.Loading -> {}
        }
    }

    private fun reduce(currentState: SettingsScreenState.DefaultState, event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.NewRoom.NewRoomClicked -> {
                launchOnMain {
//                    sendEffect(SettingsScreenEffect.OpenRoomDialog)
                    sendEffect(SettingsScreenEffect.CreateNewRoom)
                }

            }
            is SettingsScreenEvent.NewUser.NewUserClicked -> {
                launchOnMain {
                    sendEffect(SettingsScreenEffect.OpenUserDialog)
                }
            }
            SettingsScreenEvent.EmptySlotClicked -> {
            }
            SettingsScreenEvent.OpenDefaultScreen -> {
                produceDefaultState()
            }
            is SettingsScreenEvent.NewRoom.NewRoomConfirmed -> {}
            is SettingsScreenEvent.NewUser.NewUserConfirmed -> {}
        }
    }


    private fun produceDefaultState() {
        launchOnMain {
            setState(SettingsScreenState.DefaultState)
        }
    }

    fun saveNewRoom(roomName: String) {
        launchOnMain {
            setState(SettingsScreenState.Loading)
            withIo {
//                addNewRoomUseCase.run(roomName)
                createRoomUseCase.run(CreateRoomUseCase.Params(roomName, 0, "", ""))
            }
                .onSuccess {
                    sendEffect(SettingsScreenEffect.SettingsScreenSuccess.NewRoomSaveSuccess)
                }
                .onFailure {
                    sendEffect(SettingsScreenEffect.SettingsScreenError.NewRoomError(it))
                }
            setState(SettingsScreenState.DefaultState)
        }
    }

    fun saveNewUser(userName: String) {
        launchOnMain {
            setState(SettingsScreenState.Loading)
            withIo {
                createUserUseCase.run(userName)
            }
                .onSuccess {
                    sendEffect(SettingsScreenEffect.SettingsScreenSuccess.NewUserSaveSuccess)
                }
                .onFailure {
                    sendEffect(SettingsScreenEffect.SettingsScreenError.NewUserError(it))
                }
//            produceDefaultState()

            setState(SettingsScreenState.DefaultState)
        }
    }

}