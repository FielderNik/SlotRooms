package com.testing.slotrooms.utils

import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.presentation.model.SlotRoom
import java.text.SimpleDateFormat
import java.util.*

fun Long?.dateFormat(template: String = "dd MMM yyyy"): String {
    this?.let{
        val formatter = SimpleDateFormat(template, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return ""
}

fun Long.timeFormat(template: String = "HH : mm") : String {
    return SimpleDateFormat(template, Locale.getDefault()).format(Date(this))
}

fun SlotRoom.toSlotsEntity() : Slots {
    return Slots(
        id = this.id,
        startTime = this.beginDateTime,
        endTime = this.endDateTime,
        roomId = this.room.id,
        ownerId = this.owner.id,
        comment = this.comments
    )
}

fun SlotsRoomsUsersEntity.toSlotRoom() : SlotRoom {
    return SlotRoom(
        id = this.slots.id,
        room = this.room,
        owner = this.owner,
        comments = this.slots.comment,
        beginDateTime = this.slots.startTime,
        endDateTime = this.slots.endTime
    )
}

