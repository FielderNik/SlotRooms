package com.testing.slotrooms.presentation.addnewslot

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

sealed class AddNewSlotState {
    object EmptyState : AddNewSlotState()
    data class OpenSlotDialog(val dialogType: DialogType) : AddNewSlotState()
    data class DisplaySlotState(val slotRoom: SlotRoom): AddNewSlotState()
    class OpenDatePicker(val isBegin: Boolean) : AddNewSlotState()
    class OpenTimePicker(val isBegin: Boolean) : AddNewSlotState()
    class OpenCommentDialog(val comment: String): AddNewSlotState()
}

sealed interface UI
sealed interface Internal
sealed interface Effects
sealed class AddNewSlotEvent {
    class OnDialogClicked(val dialogType: DialogType) : AddNewSlotEvent(), UI
    object OnDialogDismissClicked: AddNewSlotEvent(), UI
    class DatePickerClicked(val isBegin: Boolean): AddNewSlotEvent(), UI
    class TimePickerClicked(val isBegin: Boolean): AddNewSlotEvent(), UI
    object EndDateClicked: AddNewSlotEvent(), UI
    object EndTimeClicked: AddNewSlotEvent(), UI
    object OwnerClicked: AddNewSlotEvent(), UI
    object CommentClicked: AddNewSlotEvent(), UI
    data class SelectedRoomEvent(val room: String): AddNewSlotEvent(), UI
    data class SelectedOwnerEvent(val owner: String): AddNewSlotEvent(), UI
    object EnterScreen: AddNewSlotEvent(), UI
    data class SelectedBeginDateEvent(val beginDateMillis: Long): AddNewSlotEvent(), UI
    data class SelectedBeginTimeEvent(val beginTimeHour: Int, val beginTimeMinutes: Int): AddNewSlotEvent(), UI
    data class SelectedEndDateEvent(val endDateMillis: Long): AddNewSlotEvent(), UI
    data class SelectedEndTimeEvent(val endTimeHour: Int, val endTimeMinutes: Int): AddNewSlotEvent(), UI
    data class CommentSubmittedEvent(val comment: String): AddNewSlotEvent(), UI
    object SaveSlotEvent : AddNewSlotEvent()
    object CancelSlotEvent : AddNewSlotEvent()


    data class AddNewSlotError(val message: String): AddNewSlotEvent(), Effects
}

sealed class AddNewSlotEffect {
    class ShowError(val exception: Exception): AddNewSlotEffect()
}

data class SlotRoom(
    val roomName: String = "Choice room",
    val beginDate: String = "01 янв 2022",
    val beginTime: String = "00 : 00",
    val endDate: String = "01 янв 2022",
    val endTime: String = "01 : 00",
    val owner: String = "Иванов Павел",
    val comments: String = "",
    val beginDateTime: Long = 0L,
    val endDateTime: Long = 0L,
    ) {

    companion object {
        fun getCurrentDate() : Long {
            return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000L
        }
    }
}

data class SlotDialog(
    val dialogType: DialogType = DialogType.ROOM,
    val isOpen: Boolean = false,
)

enum class DialogType {
    ROOM,
    OWNER
}
