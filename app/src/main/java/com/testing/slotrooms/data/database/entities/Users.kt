package com.testing.slotrooms.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users(
    @PrimaryKey
    val id: String,
    val name: String
)
