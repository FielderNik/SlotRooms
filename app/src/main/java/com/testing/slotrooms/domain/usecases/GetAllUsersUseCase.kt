package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Users
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(private val databaseRepository: DatabaseRepository): UseCase<Exception, List<Users>> {
    override suspend fun run(): Either<Exception, List<Users>> {
        return try {
            val users = databaseRepository.getAllUsers()
            Either.Right(users)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}