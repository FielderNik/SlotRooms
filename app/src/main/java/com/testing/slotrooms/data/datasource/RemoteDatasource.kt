package com.testing.slotrooms.data.datasource

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.data.database.entities.UserEntity
import com.testing.slotrooms.data.handlers.ResponseHandler
import com.testing.slotrooms.data.services.SlotService
import javax.inject.Inject

class RemoteDatasource @Inject constructor(
    private val service: SlotService,
    private val responseHandler: ResponseHandler
) {
    // users
    suspend fun getAllUsers(): Either<Exception, List<UserEntity>> {
        return responseHandler.handleResponse {
            service.getAllUsers()
        }
    }

    suspend fun createUser(userEntity: UserEntity) : Either<Exception, UserEntity> {
        return responseHandler.handleResponse {
            service.createUser(userEntity)
        }
    }

    //slots


    //rooms


}