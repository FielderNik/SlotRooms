package com.testing.slotrooms.core

interface UseCase<in Params, out Result> {
    suspend fun run(params: Params) : Either<Exception,Result>
}