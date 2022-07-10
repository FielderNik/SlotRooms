package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.core.flatMap
import javax.inject.Inject

class LoadUsersRoomsSlotsUseCase @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getAllSlotsUseCase: GetAllSlotsUseCase,
) : UseCase<None, None> {
    override suspend fun run(params: None): Either<Exception, None> {
        return getAllUsersUseCase.run(params).flatMap {
            getAllRoomsUseCase.run(params).flatMap {
                getAllSlotsUseCase.run(GetAllSlotsUseCase.Params(null)).flatMap {
                    Either.Right(None)
                }
            }
        }
    }
}