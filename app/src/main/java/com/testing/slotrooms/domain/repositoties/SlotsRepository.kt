package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.SlotsRoomsUsersEntity
import com.testing.slotrooms.data.services.SlotService
import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.presentation.model.SlotRoom
import javax.inject.Inject

interface SlotsRepository {
    suspend fun createSlot(slot: SlotEntity): Either<Exception, SlotEntity?>
    suspend fun updateSlot(slot: SlotEntity): Either<Exception, SlotEntity?>
    suspend fun deleteSlot(slotId: String): Either<Exception, None>
    suspend fun getAllSlots(): Either<Exception, List<SlotsRoomsUsersEntity>>
    suspend fun getSlotById(slotId: String): Either<Exception, SlotEntity?>
    suspend fun getSlotsByRoom(roomId: String): Either<Exception, List<SlotEntity>?>
    suspend fun getSlotsByTimeRange(startTime: String, endTime: String): Either<Exception, List<SlotEntity>>
    suspend fun getSlotsByTimeRangeAndRoom(roomId: String, startTime: String, endTime: String): Either<Exception, List<SlotEntity>>

    suspend fun getAllSlotRooms() : Either<Exception, List<SlotRoom>>
    suspend fun getAllSlotRoomsByRoom(roomId: String) : Either<Exception, List<SlotRoom>>
}

class SlotsRepositoryImpl @Inject constructor(
    private val dao: SlotsDao,
    private val service: SlotService
) : SlotsRepository {

    override suspend fun createSlot(slot: SlotEntity): Either<Exception, SlotEntity?> {
        return try {
            val response = service.createSlot(slot)
            if (response.isSuccessful) {
                response.body()?.let { responseSlot ->
                    cashedSlot(responseSlot)
                    Either.Right(responseSlot)
                } ?: Either.Left(SlotsException.RemoteException.ResponseException())
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun updateSlot(slot: SlotEntity): Either<Exception, SlotEntity?> {
//        TODO("Not yet implemented")
        return Either.Right(null)
    }

    override suspend fun deleteSlot(slotId: String): Either<Exception, None> {
//        TODO("Not yet implemented")
        return Either.Right(None)
    }

    override suspend fun getAllSlots(): Either<Exception, List<SlotsRoomsUsersEntity>> {
        return try {
            val response = service.getAllSlots()
            if (response.isSuccessful) {
                cashedAllSlots(response.body())
                val slots = dao.getAllSlotsRoomsUsersEntities()
                Either.Right(slots)
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun getSlotById(slotId: String): Either<Exception, SlotEntity?> {
//        TODO("Not yet implemented")
        return Either.Right(null)
    }

    override suspend fun getSlotsByRoom(roomId: String): Either<Exception, List<SlotEntity>?> {
        return try {
            val response = service.getSlotsByRoom(roomId)
            if (response.isSuccessful) {
                Either.Right(response.body())
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun getSlotsByTimeRange(startTime: String, endTime: String): Either<Exception, List<SlotEntity>> {
//        TODO("Not yet implemented")
        return Either.Right(emptyList())
    }

    override suspend fun getSlotsByTimeRangeAndRoom(roomId: String, startTime: String, endTime: String): Either<Exception, List<SlotEntity>> {
//        TODO("Not yet implemented")
        return Either.Right(emptyList())
    }

    override suspend fun getAllSlotRooms(): Either<Exception, List<SlotRoom>> {
        return getAllSlots().flatMap { slots ->
            Either.Right(slots.map(SlotsRoomsUsersEntityToSlotRoomMapper()))
        }
    }

    override suspend fun getAllSlotRoomsByRoom(roomId: String): Either<Exception, List<SlotRoom>> {
//        return getSlotsByRoom(roomId).flatMap { slots ->
//            if (slots != null) {
//                Either.Right(slots.map {
//                    it.toSlot
//                })
//            } else {
//                Either.Left(SlotsException.RemoteException.ResponseException())
//            }
//        }
        return Either.Left(SlotsException.RemoteException.ResponseException())
    }

    private suspend fun cashedSlot(slot: SlotEntity) {
        dao.insertSlot(slot = slot)
    }

    private suspend fun cashedAllSlots(slots: List<SlotEntity>?) {
        slots?.let {
            dao.deleteAllSlots()
            slots.forEach {
                dao.insertSlot(it)
            }
        }
    }

}