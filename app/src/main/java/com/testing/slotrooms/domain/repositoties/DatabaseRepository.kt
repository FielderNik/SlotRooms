package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.model.database.SlotsDao
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.Users
import javax.inject.Inject

interface DatabaseRepository {
    suspend fun insertUser(user: Users)
    suspend fun insertRoom(room: Rooms)
    suspend fun insertSlot(slot: Slots)
    suspend fun getAllUsers() : List<Users>
    suspend fun getAllRooms() : List<Rooms>

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

}