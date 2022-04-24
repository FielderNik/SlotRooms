package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.repositoties.RemoteRepository
import com.testing.slotrooms.domain.repositoties.RoomsRepository
import javax.inject.Inject

class GetAllRoomsUseCase @Inject constructor(
    private val roomsRepository: RoomsRepository,
    private val databaseRepository: DatabaseRepository,
    private val remoteRepository: RemoteRepository,
) : UseCase<None, List<RoomEntity>> {

    override suspend fun run(params: None): Either<Exception, List<RoomEntity>> {
        return try {
            roomsRepository.getAllRooms()
//            val rooms = remoteRepository.getAllRooms()
//            rooms.forEach {
//                databaseRepository.insertRoom(it)
//            }
////            val rooms = databaseRepository.getAllRooms()
//            Either.Right(rooms)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Either.Left(ex)
        }
    }
}