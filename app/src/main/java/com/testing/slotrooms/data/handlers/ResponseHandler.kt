package com.testing.slotrooms.data.handlers

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.SlotsException
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

class ResponseHandler @Inject constructor() {
    suspend fun <T> handleResponse(responseBlock: suspend () -> Response<T>): Either<Exception, T> {
        return try {
            val response = responseBlock()
            if (response.isSuccessful) {
                response.body()?.let {
                    Either.Right(it)
                } ?: Either.Left(SlotsException.RemoteException.ResponseException())
            } else {
                Either.Left(SlotsException.RemoteException.ResponseException(response.message()))
            }
        } catch (ex: SocketTimeoutException) {
            Either.Left(SlotsException.RemoteException.ServerUnavailable(ex.message))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Either.Left(ex)
        }
    }
}