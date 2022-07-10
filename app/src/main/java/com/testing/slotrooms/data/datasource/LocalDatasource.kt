package com.testing.slotrooms.data.datasource

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.data.handlers.DatabaseHandler
import javax.inject.Inject

class LocalDatasource @Inject constructor(
    private val dao: SlotsDao,
    private val databaseHandler: DatabaseHandler
) {
    suspend fun getAllUsers() : Either<Exception, List<UserEntity>> {
        return databaseHandler.handleQuery {
            dao.getAllUsers()
        }
    }

    suspend fun saveUser(user: UserEntity) : Either<Exception, None> {
        return databaseHandler.handleQuery {
            dao.insertUser(user)
        }.flatMap {
            Either.Right(None)
        }
    }

    suspend fun saveUsers(users: List<UserEntity>) : Either<Exception, None> {
        return databaseHandler.handleQuery {
            dao.deleteAllUsers()
            users.forEach { userEntity ->
                dao.insertUser(userEntity)
            }
        }.flatMap {
            Either.Right(None)
        }
    }

    suspend fun deleteAllUsers() : Either<Exception, None> {
        return databaseHandler.handleQuery {
            dao.deleteAllUsers()
        }.flatMap {
            Either.Right(None)
        }
    }

    suspend fun createUser(user: UserEntity) : Either<Exception, UserEntity> {
        return databaseHandler.handleQuery {
            dao.insertUser(user)
        }.flatMap {
            Either.Right(user)
        }
    }
}