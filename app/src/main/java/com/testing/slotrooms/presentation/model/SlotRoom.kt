package com.testing.slotrooms.presentation.model

import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

data class SlotRoom(
    val id: String = UUID.randomUUID().toString(),
    val room: RoomEntity = RoomEntity("", "", image = "", capacity = 8),
    val owner: UserEntity = UserEntity("", ""),
    val comments: String = "",
    val beginDateTime: Long = 0L,
    val endDateTime: Long = 0L,
) {

    companion object {
        fun getCurrentDate(): Long {
            return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000L
        }
    }
}

fun SlotRoom.toSlot() = SlotEntity(
    id = this.id,
    startTime = Instant.fromEpochMilliseconds(this.beginDateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
    endTime = Instant.fromEpochMilliseconds(this.endDateTime).toLocalDateTime(TimeZone.currentSystemDefault()),
    roomId = this.room.id,
    ownerId = this.owner.id,
    comment = this.comments
)
