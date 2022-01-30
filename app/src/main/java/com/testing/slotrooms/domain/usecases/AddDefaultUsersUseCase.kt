package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Users
import java.util.*
import javax.inject.Inject

class AddDefaultUsersUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    private val defaultOwners = listOf("Ivan Popov", "Pavel Ivanov", "Alexandr Petrov", "Petr Alexandrov", "Anna Ishman", "Igor Lapshov", "Alexey Borovikov")

    suspend fun run(): Either<Exception, List<Users>> {
        return try {
            val usersDb = databaseRepository.getAllUsers()
            if (usersDb.size < 2) {
                addDefaultUsers()
            }
            val updatedUsers = databaseRepository.getAllUsers()
            Either.Right(updatedUsers)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }

    private suspend fun addDefaultUsers() {
        defaultOwners.forEach { value ->
            val user = Users(id = UUID.randomUUID().toString(), name = value)
            databaseRepository.insertUser(user)
        }

    }
}