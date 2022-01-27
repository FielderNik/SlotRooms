package com.testing.slotrooms.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Slots(
    @PrimaryKey
    val id: String,
    val start: Long,
    val end: Long,
    val roomId: Int,
    val ownerId: Int,
    val comment: String
)
