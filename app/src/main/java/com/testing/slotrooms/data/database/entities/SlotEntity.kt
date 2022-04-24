package com.testing.slotrooms.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "slots")
data class SlotEntity(
    @PrimaryKey
    val id: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val roomId: String,
    val ownerId: String? = null,
    val comment: String? = null,
    var accountId: String? = "test_account_id"
)
