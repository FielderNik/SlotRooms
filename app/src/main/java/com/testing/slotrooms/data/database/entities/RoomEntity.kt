package com.testing.slotrooms.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val image: String? = null,
    val capacity: Int,
    val address: String? = "",
    val info: String? = "",
    var accountId: String? = "test_account_id"
): Parcelable
