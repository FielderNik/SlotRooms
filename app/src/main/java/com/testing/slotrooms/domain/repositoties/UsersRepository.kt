package com.testing.slotrooms.domain.repositoties

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.SlotsException
import com.testing.slotrooms.data.database.SlotsDao
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.data.services.SlotService
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
    private val dao: SlotsDao,
    private val service: SlotService
) : UsersRepository {

    override suspend fun createUser(user: UserEntity): Either<Exception, UserEntity?> {
        return try {
            val response = service.createUser(user)
            if (response.isSuccessful) {
                response.body()?.let { responseUser ->
                    cashedUser(responseUser)
                    Either.Right(responseUser)
                } ?: Either.Left(SlotsException.RemoteException.ResponseException())
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun updateUser(user: UserEntity): Either<Exception, UserEntity?> {
        return Either.Right(null)
    }

    override suspend fun deleteUser(userId: String): Either<Exception, None> {
        return Either.Right(None)
    }

    override suspend fun getAllUsers(): Either<Exception, List<UserEntity>> {
        return try {
            val response = service.getAllUsers()
            if (response.isSuccessful) {
                cashedAllUsers(response.body())
                val users = dao.getAllUsers()
                Either.Right(users)
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: Exception) {
            Either.Left(SlotsException.RemoteException.ResponseException(ex.message))
        }
    }

    override suspend fun getUserById(userId: String): Either<Exception, UserEntity?> {
        // TODO("Not yet implemented")
        return Either.Right(null)
    }

    override suspend fun getUserByName(userName: String): Either<Exception, List<UserEntity>> {
        // TODO("Not yet implemented")
        return Either.Right(emptyList())
    }

    private suspend fun cashedUser(user: UserEntity) {
        dao.insertUser(user)
    }

    private suspend fun cashedAllUsers(users: List<UserEntity>?) {
        users?.let {
            dao.deleteAllUsers()
            users.forEach { user ->
                dao.insertUser(user)
            }
        }
    }

}