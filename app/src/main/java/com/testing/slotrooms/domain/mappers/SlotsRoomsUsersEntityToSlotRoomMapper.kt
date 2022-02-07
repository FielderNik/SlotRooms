package com.testing.slotrooms.domain.mappers

import android.util.Log
import com.testing.slotrooms.model.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.model.database.entities.Users
import com.testing.slotrooms.presentation.model.SlotRoom

class SlotsRoomsUsersEntityToSlotRoomMapper : (SlotsRoomsUsersEntity) -> (SlotRoom) {

    override fun invoke(entity: SlotsRoomsUsersEntity): SlotRoom {
        Log.d("milk", "entity: $entity")
        return SlotRoom(
            id = entity.slots.id,
            room = entity.room,
            owner = entity.owner ?: Users("", ""),
            comments = entity.slots.comment,
            beginDateTime = entity.slots.startTime,
            endDateTime = entity.slots.endTime
        )
    }
}