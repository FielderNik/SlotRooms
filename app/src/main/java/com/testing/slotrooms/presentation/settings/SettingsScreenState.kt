package com.testing.slotrooms.presentation.settings


sealed class SettingsScreenState {
    object DefaultState : SettingsScreenState()
    object Loading : SettingsScreenState()

}

sealed class SettingsScreenEvent {
    object OpenDefaultScreen : SettingsScreenEvent()
    object EmptySlotClicked : SettingsScreenEvent()

    sealed class NewRoom : SettingsScreenEvent() {
        object NewRoomClicked : NewRoom()
        data class NewRoomConfirmed(val roomName: String) : NewRoom()
    }

    sealed class NewUser : SettingsScreenEvent() {
        object NewUserClicked : NewUser()
        data class NewUserConfirmed(val userName: String) : NewUser()
    }
}

sealed class SettingsScreenEffect {

    sealed class SettingsScreenError(val exception: Exception? = null) : SettingsScreenEffect() {
        class NewRoomError(ex: Exception) : SettingsScreenError()
        class NewUserError(ex: Exception) : SettingsScreenError()
    }

    sealed class SettingsScreenSuccess() : SettingsScreenEffect() {
        object NewRoomSaveSuccess : SettingsScreenSuccess()
        object NewUserSaveSuccess : SettingsScreenSuccess()
    }

    object OpenUserDialog : SettingsScreenEffect()
    object OpenRoomDialog : SettingsScreenEffect()

}