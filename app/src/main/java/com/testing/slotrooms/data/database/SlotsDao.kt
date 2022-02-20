package com.testing.slotrooms.data.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Slots
import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.database.entities.Users

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

    @Query("SELECT * FROM Rooms WHERE name = :roomName")
    suspend fun getAllRoomsByName(roomName: String) : List<Rooms>

    @Query("SELECT * FROM Users WHERE name = :userName")
    suspend fun getAllUsersByName(userName: String) : List<Users>

    @Transaction
    @Query("SELECT * FROM Slots ORDER BY startTime")
    suspend fun getAllSlotsRoomsUsersEntities() : List<SlotsRoomsUsersEntity>

    @Transaction
    @Query("SELECT * FROM Slots WHERE id = :slotId")
    suspend fun getSlotRoomById(slotId: String) : SlotsRoomsUsersEntity

    @Transaction
    @RawQuery
    suspend fun getSlotsRoomsUsers(query: SupportSQLiteQuery) : List<SlotsRoomsUsersEntity>
}