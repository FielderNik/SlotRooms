package com.testing.slotrooms.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rooms(
    @PrimaryKey
    val id: String,
    val name: String
)
