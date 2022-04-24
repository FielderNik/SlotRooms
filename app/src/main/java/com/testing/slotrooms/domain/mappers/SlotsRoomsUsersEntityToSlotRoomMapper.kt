package com.testing.slotrooms.domain.mappers

import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.presentation.model.SlotRoom
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject

class SlotsRoomsUsersEntityToSlotRoomMapper @Inject constructor() : (SlotsRoomsUsersEntity) -> (SlotRoom) {

    override fun invoke(entity: SlotsRoomsUsersEntity): SlotRoom {
        return SlotRoom(
            id = entity.slotEntity.id,
            room = entity.room,
            owner = entity.owner ?: UserEntity("", "", accountId = "test_account_id"),
            comments = entity.slotEntity.comment ?: "",
            beginDateTime = entity.slotEntity.startTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            endDateTime = entity.slotEntity.endTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }
}