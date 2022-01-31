package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Rooms
import javax.inject.Inject

class GetAllRoomsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<Exception, List<Rooms>> {

    override suspend fun run(): Either<Exception, List<Rooms>> {
        return try {
            val users = databaseRepository.getAllRooms()
            Either.Right(users)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}