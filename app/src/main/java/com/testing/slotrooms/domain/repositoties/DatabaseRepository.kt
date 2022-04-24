package com.testing.slotrooms.domain.repositoties

import androidx.sqlite.db.SimpleSQLiteQuery
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.domain.repositoties.queries.GetSlotsRoomsUsersByFilterSQLQuery
import com.testing.slotrooms.presentation.model.SlotFilter
import javax.inject.Inject

interface DatabaseRepository {
    suspend fun insertUser(user: UserEntity)
    suspend fun insertRoom(room: RoomEntity)
    suspend fun insertSlot(slot: SlotEntity)
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun getAllRooms(): List<RoomEntity>
    suspend fun getSlotsByRoomIdAndTime(slot: SlotEntity): List<SlotEntity>
    suspend fun getAllSlotsRoomsUsersEntities(): List<SlotsRoomsUsersEntity>
    suspend fun getSlotsRoomsUsers(filter: SlotFilter): List<SlotsRoomsUsersEntity>
    suspend fun getAllRoomsByName(roomName: String): List<RoomEntity>
    suspend fun getAllUsersByName(userName: String): List<UserEntity>
    suspend fun getSlotRoomById(slotId: String): SlotsRoomsUsersEntity
    suspend fun deleteSlot(slot: SlotEntity)

}

class DatabaseRepositoryImpl @Inject constructor(private val slotsDao: SlotsDao) : DatabaseRepository {
    override suspend fun insertUser(user: UserEntity) {
        user.accountId = "test_account_id"
        slotsDao.insertUser(user)
    }

    override suspend fun insertRoom(room: RoomEntity) {
        room.accountId = "test_account_id"
        slotsDao.insertRoom(room)
    }

    override suspend fun insertSlot(slot: SlotEntity) {
        slot.accountId = "test_account_id"
        slotsDao.insertSlot(slot)
    }

    override suspend fun getAllUsers(): List<UserEntity> {
        return slotsDao.getAllUsers()
    }

    override suspend fun getAllRooms(): List<RoomEntity> {
        return slotsDao.getAllRooms()
    }

    override suspend fun getSlotsByRoomIdAndTime(slot: SlotEntity): List<SlotEntity> {
        return slotsDao.getSlotsByRoomIdAndTime(slot.roomId, slot.startTime.toString(), slot.endTime.toString())
    }

    override suspend fun getAllSlotsRoomsUsersEntities(): List<SlotsRoomsUsersEntity> {
        return slotsDao.getAllSlotsRoomsUsersEntities()
    }

    override suspend fun getAllRoomsByName(roomName: String): List<RoomEntity> {
        return slotsDao.getAllRoomsByName(roomName = roomName)
    }

    override suspend fun getAllUsersByName(userName: String): List<UserEntity> {
        return slotsDao.getAllUsersByName(userName = userName)
    }

    override suspend fun getSlotRoomById(slotId: String): SlotsRoomsUsersEntity {
        return slotsDao.getSlotRoomById(slotId)
    }

    override suspend fun deleteSlot(slot: SlotEntity) {
        return slotsDao.deleteSlot(slot)
    }

    override suspend fun getSlotsRoomsUsers(filter: SlotFilter): List<SlotsRoomsUsersEntity> {
        val query = SimpleSQLiteQuery(GetSlotsRoomsUsersByFilterSQLQuery().invoke(filter))
        return slotsDao.getSlotsRoomsUsers(query)
    }


}