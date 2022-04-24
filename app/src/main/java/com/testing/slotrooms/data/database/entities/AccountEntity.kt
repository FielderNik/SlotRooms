package com.testing.slotrooms.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "account")
@Parcelize
data class AccountEntity(
    val id: String = "test_account_id",
    val name: String = "test_account_name"
) : Parcelable
