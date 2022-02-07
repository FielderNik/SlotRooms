package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Users
import java.util.*
import javax.inject.Inject

class AddNewUserUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<String, None> {
    override suspend fun run(params: String): Either<Exception, None> {
        return try {
            val users = databaseRepository.getAllUsersByName(params.trim())
            if (users.isEmpty()) {
                val newUser = Users(id = UUID.randomUUID().toString(), name = params)
                databaseRepository.insertUser(newUser)
                Either.Right(None)
            } else {
                Either.Left(SlotsException.FeatureException.UserExistsException)
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}
