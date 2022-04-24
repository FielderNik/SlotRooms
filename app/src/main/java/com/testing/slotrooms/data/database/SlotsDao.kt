package com.testing.slotrooms.data.database

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.database.entities.UserEntity

@Dao
interface SlotsDao {

    //Slots
    @Insert
    suspend fun insertSlot(slot: SlotEntity)
    //TODO исправить
    @Query("SELECT * FROM (SELECT * FROM Slots WHERE (:beginTime BETWEEN startTime AND endTime) OR (:endTime BETWEEN startTime AND endTime)) WHERE :roomId = roomId")
    suspend fun getSlotsByRoomIdAndTime(roomId: String, beginTime: String, endTime: String) : List<SlotEntity>

    @Transaction
    @Query("SELECT * FROM Slots ORDER BY startTime")
    suspend fun getAllSlotsRoomsUsersEntities() : List<SlotsRoomsUsersEntity>

    @Transaction
    @Query("SELECT * FROM Slots WHERE id = :slotId")
    suspend fun getSlotRoomById(slotId: String) : SlotsRoomsUsersEntity

    @Transaction
    @RawQuery
    suspend fun getSlotsRoomsUsers(query: SupportSQLiteQuery) : List<SlotsRoomsUsersEntity>

    @Delete
    suspend fun deleteSlot(slot: SlotEntity)

    @Query("DELETE FROM slots")
    suspend fun deleteAllSlots()

    @Query("SELECT * FROM slots ORDER BY startTime")
    suspend fun getAllSlots() : List<SlotEntity>


    //Rooms
    @Insert
    suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms() : List<RoomEntity>

    @Query("SELECT * FROM Rooms WHERE name = :roomName")
    suspend fun getAllRoomsByName(roomName: String) : List<RoomEntity>

    @Query("DELETE FROM rooms")
    suspend fun deleteAllRooms()



    //Users
    @Insert
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<UserEntity>

    @Query("SELECT * FROM Users WHERE name = :userName")
    suspend fun getAllUsersByName(userName: String) : List<UserEntity>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}