package com.testing.slotrooms.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Users(
    @PrimaryKey
    val id: String,
    val name: String
) : Parcelable
