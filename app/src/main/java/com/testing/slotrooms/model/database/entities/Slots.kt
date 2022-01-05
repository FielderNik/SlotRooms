package com.testing.slotrooms.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Slots(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val start: Int,
    val end: Int,
    val roomId: Int,
    val ownerId: Int,
    val comment: String
)
