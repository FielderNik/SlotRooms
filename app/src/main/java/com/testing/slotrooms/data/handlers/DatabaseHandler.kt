package com.testing.slotrooms.data.handlers

import com.testing.slotrooms.core.Either
import javax.inject.Inject

class DatabaseHandler @Inject constructor() {
    suspend fun <T> handleQuery(queryBlock: suspend () -> T) : Either<Exception, T> {
        return try {
            val data = queryBlock()
            Either.Right(data)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}