package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Rooms
import java.util.*
import javax.inject.Inject

class AddDefaultRoomsUseCase @Inject constructor(val databaseRepository: DatabaseRepository) : UseCase<Exception, List<Rooms>> {
    private val defaultRooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")

    override suspend fun run(): Either<Exception, List<Rooms>> {
        return try {
            val roomsDb = databaseRepository.getAllRooms()
            if (roomsDb.size < 2) {
                addDefaultRooms()
            }
            val updatedRooms = databaseRepository.getAllRooms()
            Either.Right(updatedRooms)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }

    private suspend fun addDefaultRooms() {
        defaultRooms.forEach { value ->
            val room = Rooms(id = UUID.randomUUID().toString(), name = value)
            databaseRepository.insertRoom(room)
        }
    }
}