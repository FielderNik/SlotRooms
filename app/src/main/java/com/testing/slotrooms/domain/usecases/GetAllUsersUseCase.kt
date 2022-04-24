package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.domain.repositoties.UsersRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) : UseCase<None, List<UserEntity>> {
    override suspend fun run(params: None): Either<Exception, List<UserEntity>> {
        return try {
            usersRepository.getAllUsers()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Either.Left(ex)
        }
    }
}