package com.testing.slotrooms.model.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SlotsRoomsUsersEntity(
    @Embedded val slots: Slots,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "id"
    )
    val room: Rooms,
    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id"
    )
    val owner: Users

)
