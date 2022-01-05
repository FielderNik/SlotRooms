package com.testing.slotrooms.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rooms(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val name: String
)
