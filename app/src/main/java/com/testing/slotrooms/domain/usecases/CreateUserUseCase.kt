package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.domain.repositoties.UsersRepository
import java.util.*
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) : UseCase<String, None> {
    override suspend fun run(params: String): Either<Exception, None> {
        return try {
            val newUser = UserEntity(id = UUID.randomUUID().toString(), name = params)
            return usersRepository.createUser(newUser).flatMap {
                Either.Right(None)
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}
