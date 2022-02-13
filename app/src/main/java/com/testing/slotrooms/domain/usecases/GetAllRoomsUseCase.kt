package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import javax.inject.Inject

class GetAllRoomsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<None, List<Rooms>> {

    override suspend fun run(params: None): Either<Exception, List<Rooms>> {
        return try {
            val users = databaseRepository.getAllRooms()
            Either.Right(users)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}