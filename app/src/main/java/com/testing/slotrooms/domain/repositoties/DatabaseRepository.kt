package com.testing.slotrooms.domain.repositoties

import androidx.sqlite.db.SimpleSQLiteQuery
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Slots
import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.domain.repositoties.queries.GetSlotsRoomsUsersByFilterSQLQuery
import com.testing.slotrooms.presentation.model.SlotFilter
import javax.inject.Inject

interface DatabaseRepository {
    suspend fun insertUser(user: Users)
    suspend fun insertRoom(room: Rooms)
    suspend fun insertSlot(slot: Slots)
    suspend fun getAllUsers(): List<Users>
    suspend fun getAllRooms(): List<Rooms>
    suspend fun getSlotsByRoomIdAndTime(slot: Slots): List<Slots>
    suspend fun getAllSlotsRoomsUsersEntities(): List<SlotsRoomsUsersEntity>
    suspend fun getSlotsRoomsUsers(filter: SlotFilter): List<SlotsRoomsUsersEntity>
    suspend fun getAllRoomsByName(roomName: String): List<Rooms>
    suspend fun getAllUsersByName(userName: String): List<Users>
    suspend fun getSlotRoomById(slotId: String): SlotsRoomsUsersEntity
    suspend fun deleteSlot(slot: Slots)

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
        return slotsDao.getSlotsByRoomIdAndTime(slot.roomId, slot.startTime, slot.endTime)
    }

    override suspend fun getAllSlotsRoomsUsersEntities(): List<SlotsRoomsUsersEntity> {
        return slotsDao.getAllSlotsRoomsUsersEntities()
    }

    override suspend fun getAllRoomsByName(roomName: String): List<Rooms> {
        return slotsDao.getAllRoomsByName(roomName = roomName)
    }

    override suspend fun getAllUsersByName(userName: String): List<Users> {
        return slotsDao.getAllUsersByName(userName = userName)
    }

    override suspend fun getSlotRoomById(slotId: String): SlotsRoomsUsersEntity {
        return slotsDao.getSlotRoomById(slotId)
    }

    override suspend fun deleteSlot(slot: Slots) {
        return slotsDao.deleteSlot(slot)
    }

    override suspend fun getSlotsRoomsUsers(filter: SlotFilter): List<SlotsRoomsUsersEntity> {
        val query = SimpleSQLiteQuery(GetSlotsRoomsUsersByFilterSQLQuery().invoke(filter))
        return slotsDao.getSlotsRoomsUsers(query)
    }


}