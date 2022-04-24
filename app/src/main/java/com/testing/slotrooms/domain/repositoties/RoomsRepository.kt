package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.services.SlotService
import javax.inject.Inject

interface RoomsRepository {
    suspend fun createRoom(room: RoomEntity): Either<Exception, RoomEntity?>
    suspend fun updateRoom(room: RoomEntity): Either<Exception, RoomEntity?>
    suspend fun deleteRoom(roomId: String): Either<Exception, None>
    suspend fun getAllRooms(): Either<Exception, List<RoomEntity>>
    suspend fun getRoomById(roomId: String): Either<Exception, RoomEntity?>
    suspend fun getRoomByName(roomName: String): Either<Exception, List<RoomEntity>>

}

class RoomsRepositoryImpl @Inject constructor(private val dao: SlotsDao, private val service: SlotService) : RoomsRepository {

    override suspend fun createRoom(room: RoomEntity): Either<Exception, RoomEntity?> {
        return try {
            val response = service.createRoom(room)
            if (response.isSuccessful) {
                response.body()?.let { responseRoom ->
                    cashedRoom(responseRoom)
                    Either.Right(responseRoom)
                } ?: Either.Left(SlotsException.RemoteException.ResponseException())
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun updateRoom(room: RoomEntity): Either<Exception, RoomEntity?> {
        // TODO("Not yet implemented")
        return Either.Right(null)
    }

    override suspend fun deleteRoom(roomId: String): Either<Exception, None> {
        //TODO("Not yet implemented")
        return Either.Right(None)
    }

    override suspend fun getAllRooms(): Either<Exception, List<RoomEntity>> {
        return try {
            val response = service.getAllRooms()
            if (response.isSuccessful) {
                cashedAllRooms(response.body())
                val rooms = dao.getAllRooms()
                Either.Right(rooms)
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun getRoomById(roomId: String): Either<Exception, RoomEntity?> {
        //TODO("Not yet implemented")
        return Either.Right(null)
    }

    override suspend fun getRoomByName(roomName: String): Either<Exception, List<RoomEntity>> {
        //TODO("Not yet implemented")
        return Either.Right(emptyList())
    }

    private suspend fun cashedAllRooms(rooms: List<RoomEntity>?) {
        rooms?.let {
            dao.deleteAllRooms()
            rooms.forEach { room ->
                dao.insertRoom(room)
            }
        }
    }

    private suspend fun cashedRoom(room: RoomEntity) {
        dao.insertRoom(room)
    }

}