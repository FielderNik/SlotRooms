package com.testing.slotrooms.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Slots(
    @PrimaryKey
    val id: String,
    val startTime: Long,
    val endTime: Long,
    val roomId: String,
    val ownerId: String,
    val comment: String
)
