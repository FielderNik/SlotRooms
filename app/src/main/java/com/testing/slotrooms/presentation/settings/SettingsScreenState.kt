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

    sealed class SettingsScreenError(exception: Exception? = null) : SettingsScreenEffect(exception) {
        class NewRoomError(ex: Exception) : SettingsScreenError(exception = ex)
        class NewUserError(ex: Exception) : SettingsScreenError(exception = ex)
    }

    sealed class SettingsScreenSuccess() : SettingsScreenEffect() {
        object NewRoomSaveSuccess : SettingsScreenSuccess()
        object NewUserSaveSuccess : SettingsScreenSuccess()
    }

}