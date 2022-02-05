package com.testing.slotrooms.core

sealed class SlotsException(override val message: String? = null) : Exception(message) {
    sealed class FeatureException(override val message: String? = null) : SlotsException(message = message) {
        object RoomExistsException : FeatureException()
    }
}
