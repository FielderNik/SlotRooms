package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.data.datasource.LocalDatasource
import com.testing.slotrooms.data.datasource.RemoteDatasource
import javax.inject.Inject

interface UsersRepository {
    suspend fun createUser(user: UserEntity): Either<Exception, UserEntity?>
    suspend fun updateUser(user: UserEntity): Either<Exception, UserEntity?>
    suspend fun deleteUser(userId: String): Either<Exception, None>
    suspend fun getAllUsers(): Either<Exception, List<UserEntity>>
    suspend fun getUserById(userId: String): Either<Exception, UserEntity?>
    suspend fun getUserByName(userName: String): Either<Exception, List<UserEntity>>

}

class UsersRepositoryImpl @Inject constructor(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource
) : UsersRepository {

    override suspend fun createUser(user: UserEntity): Either<Exception, UserEntity?> {
        return remoteDatasource.createUser(user).flatMap { responseUser ->
            localDatasource.createUser(responseUser)
        }
    }

    override suspend fun updateUser(user: UserEntity): Either<Exception, UserEntity?> {
        return Either.Right(null)
    }

    override suspend fun deleteUser(userId: String): Either<Exception, None> {
        return Either.Right(None)
    }

    override suspend fun getAllUsers(): Either<Exception, List<UserEntity>> {
        return remoteDatasource.getAllUsers().flatMap { users ->
            localDatasource.saveUsers(users).flatMap {
                localDatasource.getAllUsers()
            }
        }
    }

    override suspend fun getUserById(userId: String): Either<Exception, UserEntity?> {
        return Either.Right(null)
    }

    override suspend fun getUserByName(userName: String): Either<Exception, List<UserEntity>> {
        return Either.Right(emptyList())
    }

}