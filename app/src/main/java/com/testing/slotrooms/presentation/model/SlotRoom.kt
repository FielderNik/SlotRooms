package com.testing.slotrooms.presentation.model

import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Users
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

data class SlotRoom(
    val id: String = UUID.randomUUID().toString(),
    val room: Rooms = Rooms("", ""),
    val owner: Users = Users("", ""),
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