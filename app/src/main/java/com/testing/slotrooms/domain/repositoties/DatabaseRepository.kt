package com.testing.slotrooms.domain.repositoties

import android.util.Log
import com.testing.slotrooms.model.database.SlotsDao
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.model.database.entities.Users
import javax.inject.Inject

interface DatabaseRepository {
    suspend fun insertUser(user: Users)
    suspend fun insertRoom(room: Rooms)
    suspend fun insertSlot(slot: Slots)
    suspend fun getAllUsers() : List<Users>
    suspend fun getAllRooms() : List<Rooms>
    suspend fun getSlotsByRoomIdAndTime(slot: Slots) : List<Slots>
    suspend fun getAllSlotsRoomsUsersEntities() : List<SlotsRoomsUsersEntity>

}

class DatabaseRepositoryImpl @Inject constructor(private val slotsDao: SlotsDao) : DatabaseRepository {
    override suspend fun insertUser(user: Users) {
        slotsDao.insertUser(user)
    }

    override suspend fun insertRoom(room: Rooms) {
        slotsDao.insertRoom(room)
    }

    override suspend fun insertSlot(slot: Slots) {
        slotsDao.insertSlot(slot)
    }

    override suspend fun getAllUsers(): List<Users> {
        return slotsDao.getAllUsers()
    }

    override suspend fun getAllRooms(): List<Rooms> {
        return slotsDao.getAllRooms()
    }

    override suspend fun getSlotsByRoomIdAndTime(slot: Slots): List<Slots> {
//        val query = "SELECT * FROM slots WHERE roomId =:${slot.roomId} AND ((start BETWEEN ${slot.start} AND ${slot.end}) OR ('end' BETWEEN ${slot.start} AND ${slot.end}))"
//        Log.d("milk", "query: $query")
        return slotsDao.getSlotsByRoomIdAndTime(slot.roomId, slot.startTime, slot.endTime)
    }

    override suspend fun getAllSlotsRoomsUsersEntities(): List<SlotsRoomsUsersEntity> {
        return slotsDao.getAllSlotsRoomsUsersEntities()
    }


}