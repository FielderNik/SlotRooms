package com.testing.slotrooms.presentation.settings

sealed class SettingsScreenState {
    object DefaultState : SettingsScreenState()
    object NewRoomDialogOpen : SettingsScreenState()
    object NewUserDialogOpen : SettingsScreenState()
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

sealed class SettingsScreenEffect(val exception: Exception? = null) {
    data class NewRoomError(val ex: Exception) : SettingsScreenEffect(exception = ex)
}