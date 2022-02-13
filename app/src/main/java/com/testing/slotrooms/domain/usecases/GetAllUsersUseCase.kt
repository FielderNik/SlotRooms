package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.Users
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(private val databaseRepository: DatabaseRepository): UseCase<None, List<Users>> {
    override suspend fun run(params: None): Either<Exception, List<Users>> {
        return try {
            val users = databaseRepository.getAllUsers()
            Either.Right(users)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}