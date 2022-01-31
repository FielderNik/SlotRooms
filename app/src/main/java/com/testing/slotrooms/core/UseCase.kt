package com.testing.slotrooms.core

interface UseCase<L, R> {
    suspend fun run() : Either<L,R>
}