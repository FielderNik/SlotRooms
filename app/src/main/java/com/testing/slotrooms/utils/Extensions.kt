package com.testing.slotrooms.utils

import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.presentation.model.SlotRoom
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

fun SlotRoom.toSlotsEntity() : SlotEntity {
    return SlotEntity(
        id = this.id,
        startTime = Instant.fromEpochMilliseconds(this.beginDateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
        endTime = Instant.fromEpochMilliseconds(this.endDateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
        roomId = this.room.id,
        ownerId = this.owner.id,
        comment = this.comments
    )
}

