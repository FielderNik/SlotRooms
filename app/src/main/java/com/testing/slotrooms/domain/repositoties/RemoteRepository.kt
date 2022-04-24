package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.data.services.Api
import javax.inject.Inject

interface RemoteRepository {
    suspend fun createUser(user: UserEntity) : UserEntity?
    suspend fun updateUser(user: UserEntity) : UserEntity?
    suspend fun deleteUser(userId: String)
    suspend fun getAllUsers() : List<UserEntity>
    suspend fun getUserById(userId: String) : UserEntity?
    suspend fun getUserByName(name: String) : UserEntity?

    suspend fun createRoom(roomEntity: RoomEntity) : RoomEntity?
    suspend fun updateRoom(roomEntity: RoomEntity) : RoomEntity?
    suspend fun deleteRoom(roomId: String)
    suspend fun getAllRooms() : List<RoomEntity>
    suspend fun getRoomById(roomId: String) : RoomEntity?
    suspend fun getRoomByName(name: String) : RoomEntity?

    suspend fun createSlot(slotEntity: SlotEntity) : SlotEntity?
    suspend fun updateSlot(slotEntity: SlotEntity) : SlotEntity?
    suspend fun deleteSlot(slotId: String)
    suspend fun getAllSlots() : List<SlotEntity>
    suspend fun getSlotById(slotId: String) : SlotEntity?
    suspend fun getSlotsByRoomId(roomId: String) : List<SlotEntity>
}

class RemoteRepositoryImpl @Inject constructor(private val api: Api) : RemoteRepository {
    override suspend fun createUser(user: UserEntity): UserEntity? {
        return null
    }

    override suspend fun updateUser(user: UserEntity): UserEntity? {
        return null
    }

    override suspend fun deleteUser(userId: String) {

    }

    override suspend fun getAllUsers(): List<UserEntity> {
//        return api.slotService.getAllUsers()
        return emptyList()
    }

    override suspend fun getUserById(userId: String): UserEntity? {
        return null
    }

    override suspend fun getUserByName(name: String): UserEntity? {
        return null
    }

    override suspend fun createRoom(roomEntity: RoomEntity): RoomEntity? {
        return null
    }

    override suspend fun updateRoom(roomEntity: RoomEntity): RoomEntity? {
        return null
    }

    override suspend fun deleteRoom(roomId: String) {

    }

    override suspend fun getAllRooms(): List<RoomEntity> {
//        return api.slotService.getAllRooms()
        return emptyList()
    }

    override suspend fun getRoomById(roomId: String): RoomEntity? {
        return null
    }

    override suspend fun getRoomByName(name: String): RoomEntity? {
        return null
    }

    override suspend fun createSlot(slotEntity: SlotEntity): SlotEntity? {
        return null
    }

    override suspend fun updateSlot(slotEntity: SlotEntity): SlotEntity? {
        return null
    }

    override suspend fun deleteSlot(slotId: String) {

    }

    override suspend fun getAllSlots(): List<SlotEntity> {
        return emptyList()
    }

    override suspend fun getSlotById(slotId: String): SlotEntity? {
        return null
    }

    override suspend fun getSlotsByRoomId(roomId: String): List<SlotEntity> {
        return emptyList()
    }
}