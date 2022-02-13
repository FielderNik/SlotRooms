package com.testing.slotrooms.presentation.model

import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users

data class SlotFilter(
    val beginDate: Long? = null,
    val endDate: Long? = null,
    val room: Rooms? = null,
    val owner: Users? = null
)
