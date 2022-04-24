package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import java.util.*
import javax.inject.Inject

class AddDefaultRoomsUseCase @Inject constructor(val databaseRepository: DatabaseRepository) : UseCase<None, List<RoomEntity>> {
    private val defaultRooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")

    override suspend fun run(params: None): Either<Exception, List<RoomEntity>> {
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
            val room = RoomEntity(id = UUID.randomUUID().toString(), name = value, image = "", capacity = 8, "test_account_id")
            databaseRepository.insertRoom(room)
        }
    }
}