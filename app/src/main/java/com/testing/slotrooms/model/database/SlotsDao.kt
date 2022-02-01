package com.testing.slotrooms.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.Users

@Dao
interface SlotsDao {

    @Insert
    suspend fun insertUser(users: Users)

    @Insert
    suspend fun insertRoom(room: Rooms)

    @Insert
    suspend fun insertSlot(slot: Slots)

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms() : List<Rooms>

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<Users>

    @Query("SELECT * FROM (SELECT * FROM Slots WHERE (:beginTime BETWEEN startTime AND endTime) OR (:endTime BETWEEN startTime AND endTime)) WHERE :roomId = roomId")
    suspend fun getSlotsByRoomIdAndTime(roomId: String, beginTime: Long, endTime: Long) : List<Slots>
}