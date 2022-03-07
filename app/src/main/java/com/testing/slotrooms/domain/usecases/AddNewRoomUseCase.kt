package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import java.util.*
import javax.inject.Inject

class AddNewRoomUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : UseCase<String, Boolean> {

    override suspend fun run(params: String): Either<Exception, Boolean> {
        return try {
            val rooms = databaseRepository.getAllRoomsByName(roomName = params.trim())
            if (rooms.isEmpty()) {
                val newRoom = Rooms(id = UUID.randomUUID().toString(), name = params)
                databaseRepository.insertRoom(newRoom)
                Either.Right(true)
            } else {
                Either.Left(SlotsException.FeatureException.RoomExistsException)
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}