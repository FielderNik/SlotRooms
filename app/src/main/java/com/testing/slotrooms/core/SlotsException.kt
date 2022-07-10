package com.testing.slotrooms.core

sealed class SlotsException(override val message: String? = null) : Exception(message) {
    sealed class FeatureException(override val message: String? = null) : SlotsException(message = message) {
        object RoomExistsException : FeatureException()
        object UserExistsException: FeatureException()
    }

    sealed class RemoteException(override val message: String? = null) : SlotsException(message = message) {
        data class ServerUnavailable(override val message: String? = null) : RemoteException(message = message)
        class ResponseException(override val message: String? = null) : RemoteException(message = message)
        class ConnectionException(override val message: String? = null) : RemoteException(message = message)
    }
}
