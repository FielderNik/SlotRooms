package com.testing.slotrooms.presentation.model

import android.os.Parcelable
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Users
import kotlinx.parcelize.Parcelize

@Parcelize
data class SlotFilter(
    val beginDate: Long? = null,
    val endDate: Long? = null,
    val room: Rooms? = null,
    val owner: Users? = null
): Parcelable
