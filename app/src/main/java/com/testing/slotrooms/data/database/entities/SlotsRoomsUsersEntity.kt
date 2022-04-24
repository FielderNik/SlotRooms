package com.testing.slotrooms.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SlotsRoomsUsersEntity(
    @Embedded val slotEntity: SlotEntity,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "id"
    )
    val room: RoomEntity,
    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id"
    )
    val owner: UserEntity? = null

)
