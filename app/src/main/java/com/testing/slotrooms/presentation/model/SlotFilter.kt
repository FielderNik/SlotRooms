package com.testing.slotrooms.presentation.model

import androidx.compose.runtime.*
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.UserEntity

class SlotFilter(
    initBeginDate: Long? = null,
    initEndDate: Long? = null,
    initRoom: RoomEntity? = null,
    initOwner: UserEntity? = null,
) {
    var beginDate: Long? by mutableStateOf(initBeginDate)
    var endDate: Long? by mutableStateOf(initEndDate)
    var room: RoomEntity? by mutableStateOf(initRoom)
    var owner: UserEntity? by mutableStateOf(initOwner)
    var onFilterChanged: (() -> Unit)? by mutableStateOf(null)

    fun saveDataFromFilter(filter: SlotFilter?) {
        beginDate = filter?.beginDate
        endDate = filter?.endDate
        room = filter?.room
        owner = filter?.owner
    }

    fun isEmpty(): Boolean {
        return beginDate == null && endDate == null && room == null && owner == null
    }

    fun isNotEmpty(): Boolean {
        return beginDate != null || endDate != null || room != null || owner != null
    }
}

@Composable
fun rememberSlotFilter(): SlotFilter? {
    return remember {
        SlotFilter()
    }
}
