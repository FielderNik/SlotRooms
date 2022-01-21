package com.testing.slotrooms.core

interface EventHandler<T> {
    fun handleEvent(event: T)
}