package com.testing.slotrooms.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.Users

@Dao
interface SlotsDao {

    @Insert
    fun insertUser(users: Users)

    @Insert
    fun insertRoom(room: Rooms)

    @Insert
    fun insertSlot(slot: Slots)
}